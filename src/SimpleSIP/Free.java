package SimpleSIP;

import java.io.PrintWriter;

public class Free extends State {

    public Free() {
        printState();
    }

    @Override
    public State receivedInviteSendTRO(PrintWriter out) {
        if(out == null) {
            this.error(out);
            return new Free();
        }
        System.out.println("Received INVITE, Sending TRO");
        out.println("TRO");
        return new Connecting();
    }

    @Override
    public State userWantsToConnectSendInvite(PrintWriter out) {
        System.out.println("Sending INVITE");
        out.println("INVITE");
        return new Waiting();
    }

    private void printState() {
        System.out.println("- - - - - - - - - - - - - - - - - -");
        System.out.println("| Current state: SimpleSIP.Free             |");
        System.out.println("- - - - - - - - - - - - - - - - - -");
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Could not sleep");
        }
    }
}
