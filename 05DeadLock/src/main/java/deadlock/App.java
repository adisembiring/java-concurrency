package deadlock;

/**
 * Created by adisembiring on 12/8/2015.
 */
public class App {
    public static void main(String[] args) {
        final DeadLockRunner processor = new DeadLockRunner();
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                processor.firstThread();
            }
        });

        Thread t2 = new Thread(new Runnable() {
            public void run() {
               processor.secondThread();
            }
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        processor.finished();
    }
}
