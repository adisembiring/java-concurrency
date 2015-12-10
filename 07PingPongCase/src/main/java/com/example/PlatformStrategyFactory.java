package com.example;

import java.util.HashMap;

/**
 * Created by adisembiring on 12/10/2015.
 */
public class PlatformStrategyFactory {
    public enum PlatformType {
        ANDROID,
        PLAIN_JAVA
    };

    /**
     * Keep track of the type of platform.  This value won't change at
     * runtime.
     */
    private final PlatformType mPlatformType =
            System.getProperty("java.specification.vendor").indexOf("Android") >= 0
                    ? PlatformType.ANDROID
                    : PlatformType.PLAIN_JAVA;

    /**
     * This interface uses the Command pattern to create @a
     * PlatformStrategy implementations at runtime.
     */
    private interface IPlatformStrategyFactoryCommand {
        public PlatformStrategy execute();
    }

    /**
     * HashMap used to associate the PlatformType with the
     * corresponding command object whose execute() method creates the
     * appropriate type of @a PlatformStrategy subclass object.
     */
    private HashMap<PlatformType, IPlatformStrategyFactoryCommand> mPlatformStrategyMap =
            new HashMap<PlatformType, IPlatformStrategyFactoryCommand>();

    /**
     * Constructor stores the objects that perform output and
     * synchronization for a particular Java platform, such as
     * PlatformStrategyConsole or PlatformStrategyAndroid.
     */
    public PlatformStrategyFactory(final Object output) {

            mPlatformStrategyMap.put(PlatformType.PLAIN_JAVA,
                    new IPlatformStrategyFactoryCommand() {
                        // Creates the PlatformStrategyConsole.
                        public PlatformStrategy execute() {
                            return new PlatformStrategyConsole(output);
                        }
                    });
    }

    /**
     * Factory method that creates and returns a new @a
     * PlatformStrategy object based on underlying Java platform.
     */
    public PlatformStrategy makePlatformStrategy() {
        return mPlatformStrategyMap.get(mPlatformType).execute();
    }
}
