package SimpleSIP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class UserCommands {

    private final int TIMEOUT = 10000;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Scanner scan;

    private StateHandler handler;
    private ServerListener serverListener;
    private Thread otherThread;

    public UserCommands(ServerSocket socket_) {
        serverSocket = socket_;
        scan = new Scanner(System.in);
        out = null;
        in = null;
        clientSocket = null;
        handler = new StateHandler();
        serverListener = new ServerListener(serverSocket, handler);
        otherThread = new Thread(serverListener);
        otherThread.start();
    }

    public void run() {
        while (true) {
            printChoices();
            String answer = scan.nextLine();

            Commands choice = findAnswer(answer);
            switch (choice) {
                case INVITE:
                    handler.userWantsToConnectSendInvite();
                    break;
                case TRO:
                    handler.receivedInviteSendTRO();
                    break;
                case ACK:
                    handler.receivedTROSendACK(clientSocket);
                    break;
                case BYE:
                    handler.userWantsToQuitSendBYE();
                    break;
                case OK:
                    handler.receivedBYESendOK(clientSocket);
                    break;
                case EXIT:
                    System.out.println("Bye bye!");
                    System.exit(0);
                case BUSY:
                    handler.sendBusy();
                case ERROR:
                    System.err.println("Error");
                    break;
                default:
                    System.err.println("Invalid input, please try again");
            }
        }
    }

    private Commands findAnswer(String input) {
        input = input.toUpperCase();
        System.out.println(input);
        if(input.contains("INVITE")) {
            String[] arr = input.split(" ");
            if(arr.length < 3)
                return Commands.ERROR;
            else if(connectionEstablished(arr))
                return Commands.INVITE;
            else
                return Commands.ERROR;
        }

        switch(input) {
            case "TRO":
                return Commands.TRO;
            case "ACK":
                return Commands.ACK;
            case "BYE":
                return Commands.BYE;
            case "OK":
                return Commands.OK;
            case "EXIT":
                return Commands.EXIT;
            case "BUSY":
                return Commands.BUSY;
            default:
                return Commands.UNKNOWN;
        }
    }

    /**
     * If an user wants an
     * @param arr
     * @return
     */
    private boolean connectionEstablished(String[] arr) {
        String clientIP = arr[1].toLowerCase();
        int clientPort;
        try {
            clientPort = Integer.parseInt(arr[2]);
        } catch (NumberFormatException e) {
            System.err.println("Invalid integer");
            return false;
        }

        try {
            clientSocket = new Socket();
            clientSocket.connect(new InetSocketAddress(clientIP, clientPort));
            clientSocket.setSoTimeout(TIMEOUT);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String isBusy = in.readLine();
            if(isBusy.equals("BUSY"))
                return false;
        } catch (UnknownHostException e) {
            System.err.println("Unknown Host, quitting");
            return false;
        } catch (IOException e) {
            System.err.println("Failed to connect, quitting");
            return false;
        }

        handler.setOut(out);
        handler.setIn(in);
        try {
            handler.setRemoteIp(clientSocket.getInetAddress());
        }catch (Exception e){
            System.err.println("Could not set remote IP");
        }

        ClientListener clientListener = new ClientListener(in, handler, clientSocket);
        Thread thread = new Thread(clientListener);
        thread.start();

        return true;
    }

    private void printChoices() {
        System.out.println("\nPossible commands: ");
        System.out.println("INVITE <IP Address> <Port>");
        System.out.println("TRO");
        System.out.println("BUSY");
        System.out.println("ACK (After receiving TRO)");
        System.out.println("BYE");
        System.out.println("OK (AFTER BYE)");
        System.out.println("EXIT (Quit program)\n");
    }

    private void restartOtherThread() {
        /*
        Listener newListener = new Listener(serverSocket, true, clientSocket, out, in);
        Thread thread = new Thread(newListener);
        otherThread = thread;
        listener = newListener;
        thread.start();
        */
    }
}
