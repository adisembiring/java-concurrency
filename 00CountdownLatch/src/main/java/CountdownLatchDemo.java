import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by adisembiring on 12/7/2015.
 */
public class CountdownLatchDemo {
    public static class Processor implements Runnable {
        private final int id;
        private CountDownLatch latch;

        public Processor(CountDownLatch latch, int i) {
            this.latch = latch;
            this.id = i;
        }

        public void run() {
            System.out.println("Started by " + id);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
        }
    }

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(100);
        ExecutorService executor = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            executor.submit(new Processor(latch, i));
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Latch Done");
    }
}
