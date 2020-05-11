package SimpleSIP;

import java.io.IOException;
import java.net.ServerSocket;

public class SimpleSIPMain {
    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Please enter argument [port number]");
            System.exit(1);
        }

        int port = 0;
        ServerSocket serverSocket = null;
        try {
            port = Integer.parseInt(args[0]);
            serverSocket = new ServerSocket(port);
            System.out.println("SimpleSIP.Waiting for a connection...");
        } catch (IOException e) {
            System.err.println("Could not listen on port " + port);
            System.exit(1);
        } catch (NumberFormatException e) {
            System.err.println("Port number must be an integer!");
            System.exit(1);
        }
        // ServerSocket shouldn't need a timeout
        /*
        try {
            serverSocket.setSoTimeout(200);
        } catch (SocketException e) {
            System.out.println("Could not set timeout");
        }
        */
        UserCommands userCommands = new UserCommands(serverSocket);
        userCommands.run();
    }
}
