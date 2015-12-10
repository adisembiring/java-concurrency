package com.example;

/**
 * Created by adisembiring on 12/10/2015.
 */
public class GameOption {
    private static GameOption instance = new GameOption();

    private int maxIterations;
    private String mechanism;

    private GameOption() {

    }

    public void parseArg(String[] args) {
        maxIterations = Integer.parseInt(args[0]);
        mechanism = args[1];
    }

    public static GameOption getInstance() {
        return instance;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public String getMechanism() {
        return mechanism;
    }
}
