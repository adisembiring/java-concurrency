package com.example;

import java.util.concurrent.Semaphore;

/**
 * Created by adisembiring on 12/10/2015.
 */
public class PingPongThreadSema extends PingPongThread {
    private Semaphore mine;
    private Semaphore other;

    public PingPongThreadSema(String textToPrint, Semaphore mine, Semaphore other, int maxIterations) {
        super(textToPrint, maxIterations);
        this.mine = mine;
        this.other = other;
    }

    @Override
    protected void acquire() {
        mine.acquireUninterruptibly();
    }

    @Override
    protected void release() {
        other.release();
    }
}
