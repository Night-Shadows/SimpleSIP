import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class StateHandler {

    private State state;
    private BufferedReader in;
    private PrintWriter out;
    private InetAddress remoteIp;

    public StateHandler() {
        in = null;
        out = null;
        state = new Free();
    }

    public void userWantsToConnectSendInvite() {
        state = state.userWantsToConnectSendInvite(out);
    }

    public void receivedInviteSendTRO() {
        state = state.receivedInviteSendTRO(out);
    }

    public void receivedTROSendACK(Socket clientSocket) {
        state = state.receivedTROSendACK(out, remoteIp, clientSocket);
    }

    public void userWantsToQuitSendBYE() {
        state = state.userWantsToQuitSendBYE(out);
    }

    public void receivedBYESendOK(Socket clientSocket) {
        state = state.receivedBYESendOK(out, clientSocket);
        in = null;
        out = null;
    }

    public void receivedACKSendNothing(Socket clientSocket) {
        state = state.receivedACKSendNothing(remoteIp, clientSocket);
    }

    public void receivedOKsendNothing(Socket clientSocket) {
        in = null;
        out = null;
        state = state.receivedOKsendNothing(clientSocket);
    }

    public void errorReceived(Socket clientSocket) {
        state = state.errorReceived(out, clientSocket);
    }

    public void noInputGoToFree() {
        state = new Free();
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public InetAddress getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(InetAddress remoteIp) {
        this.remoteIp = remoteIp;
    }


    public boolean isBusy() {
        return state.busy();
    }

    public void sendBusy() {
        state = state.sendBusy(out);
    }

    public void printState() {
        if(state instanceof Free) {
            System.out.println("State is Free");
        } else if(state instanceof Connecting) {
            System.out.println("State is Connecting");
        } else if(state instanceof Waiting) {
            System.out.println("State is Waiting");
        } else if(state instanceof CallEstablished) {
            System.out.println("State is CallEstablished");
        } else if(state instanceof Quitting) {
            System.out.println("State is Quitting");
        } else {
            System.out.println("State is UNKNOWN");
        }
    }
}
