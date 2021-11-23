package me.aov.objects;

import me.aov.AfkFishing;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ChairDescription {

    private AfkFishing main;
    private String name;
    private String display;
    private int time;
    private ArrayList<String> hologramLines;
    private String permission;
    private RewardTable rewardTable;
    private double hologramOffset;
    private double chairOffset;
    private ArrayList<ItemStack> itemStacks;

    public ChairDescription(FileConfiguration fileConfiguration,String name, AfkFishing main) {
        this.main = main;
        this.name = name;
        this.display = fileConfiguration.getString("display");
        this.time = fileConfiguration.getInt("time");
        this.rewardTable = main.getDataManager().getRewardTableSet().get(fileConfiguration.getString("reward-table"));
        this.hologramLines = (ArrayList<String>) fileConfiguration.getStringList("hologram-lines");
        this.hologramOffset = fileConfiguration.getDouble("hologram-offset");
        this.chairOffset = fileConfiguration.getDouble("chair-offset");
        if(rewardTable == null){
            rewardTable = main.getDataManager().getRewardTableSet().get("default rewards table");
        }
        if(fileConfiguration.contains("permission")){
            this.permission = fileConfiguration.getString("permissin");
        }else{
            permission = "";
        }
        itemStacks = main.getDataManager().getItemStacksFromFiles(name);
    }

    public ChairDescription(AfkFishing main, RewardTable rewardTable) {
        this.main = main;
        this.name = "Dummy Chair";
        this.display = "Dummy Chair";
        this.time = 69;
        this.rewardTable = rewardTable;
        this.hologramLines = new ArrayList<>();
        this.chairOffset = .69;
        this.hologramOffset = .69;
        itemStacks = new ArrayList<>();
    }

    public void moveHologram(Boolean b){
        if(b){
            setHologramOffset(getHologramOffset()+.25);
        }else{
            setHologramOffset(getHologramOffset()-.25);
        }
        File f = new File(main.getDataManager().getDescriptionsPath().toFile(), getName()+".yml");
        FileConfiguration configuration = (FileConfiguration) new YamlConfiguration();
        try {
            configuration.load(f);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
        configuration.set("hologram-offset", getHologramOffset());
        try {
            configuration.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(Chair ch : main.getChairManager().getChairsList()){
            if(ch.getChairDescription().equals(this)){
                ch.setHologramLocation(ch.getChairLocation().clone().add(.5,hologramOffset,.5));
                ch.updateHologram();
            }
        }
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

    public void setHologramOffset(double hologramOffset) {
        this.hologramOffset = hologramOffset;
    }

    public String getPermission() {
        return permission;
    }
}
