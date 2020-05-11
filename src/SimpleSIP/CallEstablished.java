package SimpleSIP;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class CallEstablished extends BusyState {
    private InetAddress serverIp;
    private int port;
    private AudioStreamUDP audioStreamUDP;

    private KeepAlive keepAlive;
    /**
     * Starts a call
     * @param ip
     */
    public CallEstablished(InetAddress ip, PrintWriter out) {
        this.serverIp = ip;
        this.port = 2020;

        printState();
        startRtp();

        keepAlive = new KeepAlive(out);
        Thread thread = new Thread(keepAlive);
        thread.start();
    }


    @Override
    public State receivedBYESendOK(PrintWriter out, Socket clientSocket) {
        System.out.println("Received BYE, sending OK");
        out.println("OK");
        stopRtp();
        try {
            if(clientSocket != null)
                clientSocket.close();
        } catch (IOException e) {
            System.err.println("Could not close socket");
        }
        return new Free();
    }

    @Override
    public State userWantsToQuitSendBYE(PrintWriter out) {
        System.out.println("User wants to quit, sending BYE");
        out.println("BYE");
        stopRtp();
        return new Quitting();
    }

    @Override
    public State errorReceived(PrintWriter out, Socket clientSocket) {
        return super.errorReceived(null, clientSocket);
    }

    @Override
    public void error(PrintWriter out) {
        stopRtp();
        super.error(out);
    }

    private void startRtp(){
        try {
            this.audioStreamUDP = new AudioStreamUDP();
            if(serverIp != null) {
                audioStreamUDP.connectTo(serverIp, port);
                audioStreamUDP.startStreaming();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRtp(){
        if(audioStreamUDP != null) {
            audioStreamUDP.stopStreaming();
            audioStreamUDP.close();
        }
        keepAlive.setKeepAlive(false);
    }



    private void printState() {
        System.out.println("- - - - - - - - - - - - - - - - - -");
        System.out.println("| Current state: SimpleSIP.CallEstablished  |");
        System.out.println("- - - - - - - - - - - - - - - - - -");
    }
}
