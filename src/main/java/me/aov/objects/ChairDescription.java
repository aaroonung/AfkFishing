package me.aov.objects;

import me.aov.AfkFishing;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;

public class ChairDescription {

    private AfkFishing main;
    private String name;
    private String display;
    private int time;
    private ArrayList<String> hologramLines;
    private RewardTable rewardTable;
    private double hologramOffset;
    private double chairOffset;
    private ArrayList<ItemStack> itemStacks;

    public ChairDescription(FileConfiguration fileConfiguration,String name, AfkFishing main) {
        this.main = main;
        this.name = name;
        this.display = fileConfiguration.getString("display");
        this.time = fileConfiguration.getInt("time");
        this.rewardTable = main.getDataManager().rewardTableSet.get(fileConfiguration.getString("reward-table"));
        this.hologramLines = (ArrayList<String>) fileConfiguration.getStringList("hologram-lines");
        this.hologramOffset = fileConfiguration.getDouble("hologram-offset");
        this.chairOffset = fileConfiguration.getDouble("chair-offset");
        if(rewardTable == null){
            rewardTable = main.getDataManager().rewardTableSet.get("default rewards table");
        }
        itemStacks = main.getDataManager().getItemStacksFromFiles(name);

    }

    public ArrayList<ItemStack> getItemStacks() {
        return itemStacks;
    }

    public void setItemStacks(ArrayList<ItemStack> itemStacks) {
        this.itemStacks = itemStacks;
    }

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

    public double getHologramOffset() {
        return hologramOffset;
    }

    public double getChairOffset() {
        return chairOffset;
    }
}
