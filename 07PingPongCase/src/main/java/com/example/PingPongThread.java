package com.example;

/**
 * Created by adisembiring on 12/10/2015.
 */
public abstract class PingPongThread extends Thread {
    private final int maxIterations;
    private final String textToPrint;

    public PingPongThread(String textToPrint, int maxIterationst) {
        this.maxIterations = maxIterationst;
        this.textToPrint = textToPrint;
    }

    protected abstract void acquire();
    protected abstract void release();
    
    public void run() {
        for (int i = 0; i < maxIterations; i++) {
            acquire();
            PlatformStrategy
                    .instance()
                    .print(textToPrint + "(" + i + ")");
            release();
        }
        PlatformStrategy.instance().done();
    }
}
