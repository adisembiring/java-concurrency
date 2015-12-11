package com.example;

/**
 * Created by adisembiring on 12/10/2015.
 */
public class App {
    public static void main(String[] args) {
        PlatformStrategy.instance(new PlatformStrategyFactory
                        (System.out).makePlatformStrategy());


        GameOption.getInstance().parseArg(args);
        PlayPingPong pingPong = new PlayPingPong(GameOption.getInstance().getMaxIterations(), GameOption.getInstance().getMechanism());
        Thread appThread = new Thread(pingPong);
        appThread.start();
        try {
            appThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("App Exit!");
    }
}