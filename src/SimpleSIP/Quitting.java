package SimpleSIP;

import java.io.IOException;
import java.net.Socket;

public class Quitting extends State {

    public Quitting() {
        printState();
    }

    @Override
    public State receivedOKsendNothing(Socket clientSocket) {
        // TODO: Received OK in SimpleSIP.Quitting state, return nothing and end the call
        System.out.println("Received OK, now available");
        try {
            if(clientSocket != null)
                clientSocket.close();
        } catch (IOException e) {
            System.err.println("Could not close socket");
        }
        return new Free();
    }

    private void printState() {
        System.out.println("- - - - - - - - - - - - - - - - - -");
        System.out.println("| Current state: SimpleSIP.Quitting         |");
        System.out.println("- - - - - - - - - - - - - - - - - -");
    }
}
