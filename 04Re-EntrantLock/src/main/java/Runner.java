import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by adisembiring on 12/8/2015.
 */
public class Runner {
    private int count = 0;
    private Lock lock = new ReentrantLock();
    private Condition cond = lock.newCondition();

    private void increament() {
        for (int i = 0; i <10000 ; i++) {
            count++;
        }
    }

    public void firstTheard() throws InterruptedException {
        lock.lock();
        System.out.println("Waiting ... ");
        cond.await();
        System.out.println("Woken up");
        try {
            increament();
        } finally {
            lock.unlock();
        }
    }

    public void secondTheard() throws InterruptedException {
        Thread.sleep(1000);
        lock.lock();

        System.out.printf("Press the return key");
        new Scanner(System.in).nextLine();
        System.out.println("Got return key");
        cond.signal();
        try {
            increament();
        } finally {
            lock.unlock();
        }
    }

    public void finished() {
        System.out.println("count is:: " + count);
    }
}
