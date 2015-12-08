package solution2;

import demo.Acccount;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by adisembiring on 12/8/2015.
 */
public class TransferRunner2 {
    private Acccount acc1 = new Acccount();
    private Acccount acc2 = new Acccount();

    private Lock lock1 = new ReentrantLock();
    private Lock lock2 = new ReentrantLock();

    public void firstThread() throws InterruptedException {
        Random rnd = new Random();
        for (int i = 0; i < 10000; i++) {
            acquireLock(lock1, lock2);
            Acccount.transfer(acc1, acc2, rnd.nextInt(100));
            lock1.unlock();
            lock2.unlock();
        }
    }

    public void secondThread() throws InterruptedException {
        Random rnd = new Random();
        for (int i = 0; i < 10000; i++) {
            acquireLock(lock1, lock2);
            Acccount.transfer(acc2, acc1, rnd.nextInt(100));
            lock1.unlock();
            lock2.unlock();
        }
    }

    public void finished() {
        System.out.printf("Total account balance: " + (acc1.getBalance() + acc2.getBalance()));
    }

    private void acquireLock(Lock firstLock, Lock secondLock) throws InterruptedException {
        while (true) {
            boolean gotFirstLock = false;
            boolean gotSecondLock = false;
            try {
                gotFirstLock = firstLock.tryLock();
                gotSecondLock = secondLock.tryLock();
            } finally {
                if (gotFirstLock && gotSecondLock) {
                    return;
                }
                if (gotFirstLock) {
                    firstLock.unlock();
                }
                if (gotSecondLock) {
                    secondLock.unlock();
                }
            }

            Thread.sleep(1);
        }
    }
}
