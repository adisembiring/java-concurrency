package com.example;

/**
 * Created by adisembiring on 12/10/2015.
 */
public abstract class PlatformStrategy {
    public static final int NUMBER_OF_THREADS = 2;
    private static PlatformStrategy sUniqueInstance = null;

    public static PlatformStrategy instance() {
        return sUniqueInstance;
    }

    public static PlatformStrategy instance(PlatformStrategy platform) {
        return sUniqueInstance = platform;
    }

    public abstract void begin();
    public abstract void print(String outputString);
    public abstract void done();
    public abstract void awaitDone();
    public abstract void errorLog(String javaFile,
                                  String errorMessage);
    protected PlatformStrategy() {}
}
