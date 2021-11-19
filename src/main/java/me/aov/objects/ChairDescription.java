package me.aov.objects;

import java.util.ArrayList;

public class ChairDescription {

    private String name;
    private String display;
    private int time;
    private ArrayList<String> hologramLines;
    private RewardTable rewardTable;

    public String getName() {
        return name;
    }

    public int getTime() {
        return time;
    }

    public ArrayList<String> getHologramLines() {
        return hologramLines;
    }

    public RewardTable getRewardTable() {
        return rewardTable;
    }

    public String getDisplay() {
        return display;
    }
}
