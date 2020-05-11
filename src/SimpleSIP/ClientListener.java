package SimpleSIP;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientListener implements Runnable {

    private BufferedReader in;
    private StateHandler handler;
    private Socket clientSocket;

    public ClientListener(BufferedReader in_, StateHandler handler_, Socket clientSocket_) {
        in = in_;
        handler = handler_;
        clientSocket = clientSocket_;
        System.out.println(clientSocket.toString());
    }

    @Override
    public void run() {
        String port = "";
        String inputLine;
        try {
            while (((inputLine = in.readLine()) != null) && !clientSocket.isClosed()) {
                if(!inputLine.equals("ALIVE"))
                    System.out.println("RECEIVED: " + inputLine);
                if(inputLine.startsWith("port")){
                    String[] array = inputLine.split("port");
                    if(array.length > 0)
                        port = array[1];
                }

                switch (inputLine) {
                    case "INVITE":
                        break;
                    case "TRO":
                        break;
                    case "ACK":
                        handler.receivedACKSendNothing(clientSocket);
                        break;
                    case "BYE":
                        break;
                    case "OK":
                        handler.receivedOKsendNothing(clientSocket);
                        break;
                    case "BUSY":
                        closeSocket();
                        break;
                    case "ALIVE":
                        break;
                    default:
                        handler.errorReceived(clientSocket);
                        break;
                }
            }
        } catch (SocketTimeoutException e){
            System.err.println("Timeout");
            handler.errorReceived(clientSocket);
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            System.out.println("Socket closed");
            handler.errorReceived(clientSocket);
            Thread.currentThread().interrupt();
            return;
        }

        handler.noInputGoToFree();
    }

    private void closeSocket() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Could not close socket");
        }
    }
}
