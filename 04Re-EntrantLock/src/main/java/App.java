/**
 * Created by adisembiring on 12/8/2015.
 */
public class App {
    public static void main(String[] args) {
        final Runner processor = new Runner();
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    processor.firstTheard();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                try {
                    processor.secondTheard();
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
