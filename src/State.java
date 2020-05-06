import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public abstract class State {

    public State receivedInviteSendTRO(PrintWriter out) {
        error(out);
        return new Free();
    }

    public State receivedACKSendNothing(InetAddress ip, Socket clientSocket) {
        error(null);
        return new Free();
    }

    public State userWantsToConnectSendInvite(PrintWriter out) {
        error(out);
        return new Free();
    }

    public State receivedTROSendACK(PrintWriter out, InetAddress ip, Socket clientSocket) {
        error(out);
        return new Free();
    }

    public State receivedBYESendOK(PrintWriter out, Socket clientSocket) {
        error(out);
        return new Free();
    }

    public State userWantsToQuitSendBYE(PrintWriter out) {
        error(out);
        return new Free();
    }

    public State receivedOKsendNothing(Socket clientSocket) {
        error(null);
        return new Free();
    }

    public State errorReceived(PrintWriter out, Socket clientSocket) {
        error(out);
        try {
            if(clientSocket != null)
                clientSocket.close();
        } catch (IOException e) {
            System.err.println("Could not close socket");
        }
        return new Free();
    }

    public boolean busy() {
        return false;
    }

    public State sendBusy(PrintWriter out) {
        if(out != null)
            out.println("BUSY");
        return new Free();
    }

    protected void error(PrintWriter out) {
        if(out != null)
            out.println("ERROR");
        System.err.println("ERROR! Unexpected response!");
    }
}
