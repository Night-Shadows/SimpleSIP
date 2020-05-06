import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

public class Connecting extends BusyState {

    public Connecting() {
        printState();
    }

    @Override
    public State receivedACKSendNothing(InetAddress ip, Socket clientSocket) {
        System.out.println("Received ACK, call established!");
        PrintWriter out = getOutputStream(clientSocket);
        return new CallEstablished(ip, out);
    }

    private PrintWriter getOutputStream(Socket clientSocket) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    private void printState() {
        System.out.println("- - - - - - - - - - - - - - - - - -");
        System.out.println("| Current state: Connecting       |");
        System.out.println("- - - - - - - - - - - - - - - - - -");
    }
}
