package deadlock;

import demo.Acccount;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by adisembiring on 12/8/2015.
 */
public class DeadLockRunner {
    private Acccount acc1 = new Acccount();
    private Acccount acc2 = new Acccount();

    private Lock lock1 = new ReentrantLock();
    private Lock lock2 = new ReentrantLock();
    
    public void firstThread() {
        Random rnd = new Random();
        for (int i = 0; i < 10000; i++) {
            lock1.lock();
            lock2.lock();
            Acccount.transfer(acc1, acc2, rnd.nextInt(100));
            lock1.unlock();
            lock2.unlock();
        }
    }

    public void secondThread() {
        Random rnd = new Random();
        for (int i = 0; i < 10000; i++) {
            lock2.lock();
            lock1.lock();
            Acccount.transfer(acc2, acc1, rnd.nextInt(100));
            lock2.unlock();
            lock1.unlock();
        }
    }

    public void finished() {
        System.out.printf("Total account balance: " + (acc1.getBalance() + acc2.getBalance()));
    }
}
