import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

public class Waiting extends State {
    public Waiting() {
        printState();
    }

    @Override
    public State receivedTROSendACK(PrintWriter out, InetAddress ip, Socket clientSocket) {
        System.out.println("Received TRO, sending ACK");
        out.println("ACK");
        return new CallEstablished(ip, out);
    }

    private void printState() {
        System.out.println("- - - - - - - - - - - - - - - - - -");
        System.out.println("| Current state: Waiting          |");
        System.out.println("- - - - - - - - - - - - - - - - - -");
    }
}
