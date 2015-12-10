package com.example;

import java.io.PrintStream;
import java.util.concurrent.CountDownLatch;

/**
 * Created by adisembiring on 12/10/2015.
 */
public class PlatformStrategyConsole extends PlatformStrategy {
    private CountDownLatch exitBarrier = null;
    private final PrintStream mOutput;

    public PlatformStrategyConsole(Object output) {
        mOutput = (PrintStream) output;
    }

    @Override
    public void begin() {
        exitBarrier = new CountDownLatch(NUMBER_OF_THREADS);
    }

    @Override
    public void print(String outputString) {
        mOutput.println(outputString);
    }

    @Override
    public void done() {
        exitBarrier.countDown();
    }

    @Override
    public void awaitDone() {
        try {
            exitBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void errorLog(String javaFile, String errorMessage) {
        mOutput.println(javaFile
                + " "
                + errorMessage);
    }
}
