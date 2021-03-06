package com.example;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by adisembiring on 12/10/2015.
 */
public class PlayPingPong implements Runnable  {
    /**
     * Number of iterations to ping/pong.
     */
    private final static int MAX_PING_PONG_THREADS = 0;
    /**
     * Constants used to distinguish between ping and pong threads.
     */
    private final static int PING_THREADS = 0;
    private final static int PONG_THREADS = 1;

    private int maxIterations;
    private String mechanism;

    public PlayPingPong(int maxIterations, String mechanism) {
        this.maxIterations = maxIterations;
        this.mechanism = mechanism;
    }

    public void run() {
        PlatformStrategy.instance().begin();
        System.out.println("Ready, set, go!");
        PingPongThread[] pingPongThreads = new PingPongThread[2];
        createPingPongThreads(pingPongThreads);
        for (PingPongThread thread : pingPongThreads) {
            thread.start();
        }
        PlatformStrategy.instance().awaitDone();
        PlatformStrategy.instance().print("Done!");
    }

    private void createPingPongThreads(PingPongThread[] pingPongThreads) {
        createWithSemaphoreStrategy(pingPongThreads);
    }

    private void createWithSemaphoreStrategy(PingPongThread[] pingPongThreads) {
        if ("SEMA".equals(mechanism)) {
            Semaphore pingSema = new Semaphore(1);
            Semaphore pongSema = new Semaphore(0);
            pingPongThreads[PING_THREADS] = new PingPongThreadSema("ping", pingSema, pongSema, maxIterations);
            pingPongThreads[PONG_THREADS] = new PingPongThreadSema("pong", pongSema, pingSema, maxIterations);
        } else if ("COND".equals(mechanism)) {
            ReentrantLock lock = new ReentrantLock();
            Condition pingCond = lock.newCondition();
            Condition pongCond = lock.newCondition();
            PingPongThreadCond pingThread = new PingPongThreadCond("ping", lock, pingCond, pongCond, true, maxIterations);
            PingPongThreadCond pongThread = new PingPongThreadCond("pong", lock, pongCond, pingCond, false, maxIterations);
            pingThread.setOtherThreadId(pongThread.getId());
            pongThread.setOtherThreadId(pingThread.getId());
            pingPongThreads[PING_THREADS] = pingThread;
            pingPongThreads[PONG_THREADS] = pongThread;
        }
    }
}
