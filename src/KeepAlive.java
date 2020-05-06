import java.io.PrintWriter;

public class KeepAlive implements Runnable {

    private PrintWriter out;
    private boolean keepAlive;

    public KeepAlive(PrintWriter out_) {
        keepAlive = true;
        out = out_;
    }

    @Override
    public void run() {
        while(keepAlive) {
            out.println("ALIVE");
            sleep(5);
        }
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(1000*seconds);
        } catch (InterruptedException e) {
            System.err.println("Thread could not sleep");
        }
    }

    public void setKeepAlive(boolean value) {
        keepAlive = value;
    }
}
