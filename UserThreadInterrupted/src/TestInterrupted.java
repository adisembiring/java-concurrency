/**
 * Created by USER on 10/4/2015.
 */
public class TestInterrupted {
    public static void main(String[] args) {
        System.out.println("Entering main()");

        final boolean interuptedThread = args.length == 0;
        GCDRunnable runnableCommand = new GCDRunnable();
        Thread thread = new Thread(runnableCommand);
        thread.start();

        try {
            if (interuptedThread) {
                Thread.sleep(4000);
                System.out.println("Interrupting thread " + thread.getName());
                thread.interrupt();
            }
            Thread.sleep(1000);
        } catch (InterruptedException ex) {}

        System.out.println("Leaving main()");
    }
}
