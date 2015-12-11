package com.example;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by adisembiring on 12/11/2015.
 */
public class PingPongThreadCond extends PingPongThread {

    private final ReentrantLock lock;
    private final Condition mineCond;
    private final Condition otherCond;
    private final boolean isOwner;
    private static long mTurnOwner;
    private long mOtherThreadId;

    public PingPongThreadCond(String stringToPrint,
                              ReentrantLock lock,
                              Condition mineCond,
                              Condition otherCond,
                              boolean isOwner,
                              int maxIterations) {
        super(stringToPrint, maxIterations);
        this.lock = lock;
        this.mineCond = mineCond;
        this.otherCond = otherCond;
        this.isOwner = isOwner;
        if (isOwner)
            mTurnOwner = this.getId();
    }

    @Override
    protected void acquire() {
        lock.lock();
        while (mTurnOwner != this.getId()) {
            mineCond.awaitUninterruptibly();
        }
        lock.unlock();
    }

    @Override
    protected void release() {
        lock.lock();
        mTurnOwner = mOtherThreadId;
        otherCond.signal();
        lock.unlock();
    }

    public void setOtherThreadId(long otherThreadId) {
        mOtherThreadId = otherThreadId;
    }
}
