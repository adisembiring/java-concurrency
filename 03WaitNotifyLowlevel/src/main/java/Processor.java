import java.util.*;

/**
 * Created by adisembiring on 12/8/2015.
 * This is alternative of using synchronize keyword
 */
public class Processor {
    private LinkedList<Integer> list = new LinkedList<Integer>();
    private final int LIMIT = 10;
    private Object lock = new Object();

    public void producer() throws InterruptedException {
        int value = 0;
        while (true) {
            synchronized (lock) {
                while (list.size() == LIMIT) {
                    lock.wait();
                }
                list.add(value++);
                lock.notify();
            }
        }
    }

    public void consumer() throws InterruptedException {
        Random rnd = new Random();
        while (true) {
            synchronized (lock) {
                while (list.size() == 0 ) {
                    lock.wait();
                }
                System.out.print("List size is: " + list.size());
                int value = list.removeFirst();
                System.out.println("; value is: " + value);
                lock.notify();
            }
            Thread.sleep(rnd.nextInt(2000));
        }
    }

}
