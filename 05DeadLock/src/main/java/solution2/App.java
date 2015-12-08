package solution2;

/**
 * Created by adisembiring on 12/8/2015.
 */
public class App {
    public static void main(String[] args) {
        final TransferRunner2 processor = new TransferRunner2();
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    processor.firstThread();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                try {
                    processor.secondThread();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
