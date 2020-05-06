import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ServerListener implements Runnable {

    private final int TIMEOUT = 10000;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private StateHandler handler;

    public ServerListener(ServerSocket serverSocket_, StateHandler handler_) {
        serverSocket = serverSocket_;
        handler = handler_;
        clientSocket = null;
    }

    @Override
    public void run() {
        while(true) {
            try {
                clientSocket = serverSocket.accept();
                if(handler.isBusy()) {
                    PrintWriter tempOut = new PrintWriter(clientSocket.getOutputStream(), true);
                    tempOut.println("BUSY");
                    tempOut.close();
                    clientSocket.close();
                    clientSocket = null;
                } else {
                    try {
                        clientSocket.setSoTimeout(TIMEOUT);
                        out = new PrintWriter(clientSocket.getOutputStream(), true);
                        out.println("CONNECT");
                        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    } catch (IOException e) {
                        System.err.println("Couldn't create in and out streams");
                        if(clientSocket != null)
                            clientSocket.close();
                    }
                    handler.setIn(in);
                    handler.setOut(out);
                    handler.setRemoteIp(clientSocket.getInetAddress());
                    ClientListener clientListener = new ClientListener(in, handler, clientSocket);
                    Thread thread = new Thread(clientListener);
                    thread.start();
                }
            } catch(IOException e) {
                handler.errorReceived(clientSocket);
                e.printStackTrace();
            }
        }
    }
}
