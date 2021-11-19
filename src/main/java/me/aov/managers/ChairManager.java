package me.aov.managers;

import me.aov.AfkFishing;
import me.aov.task.HologramUpdaterTask;
import me.aov.objects.Chair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class ChairManager {

    private HashSet<Chair> chairsList;
    private HashSet<Location> chairLocations;
    private HashMap<UUID,Chair> playersInChairs;
    private AfkFishing main;
    private int taskInt;

    public ChairManager(AfkFishing main) {
        this.main = main;
        chairsList = new HashSet<>();
        chairLocations = new HashSet<>();
        playersInChairs = new HashMap<>();
        taskInt = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new HologramUpdaterTask(main), 40L, 20L);
    }


    public boolean isChair(Location location){
        return chairLocations.contains(location);
    }

    public Chair getChairAt(Location location){
        if(isChair(location)){
            for(Chair ch : chairsList){
                if(ch.getChairLocation().equals(location)){
                    return ch;
                }
            }
        }
        return null;
    }

    public void sitInChair(Player player, Chair chair){
        playersInChairs.put(player.getUniqueId(), chair);
        chair.sit(player);
    }

    public void sitInChair(Player player, Location location){
        Chair ch = getChairAt(location);
        if(isChair(location) && ch != null){
            playersInChairs.put(player.getUniqueId(), ch);
            ch.sit(player);
        }
    }

    public void removePlayer(Player player){
        this.playersInChairs.get(player.getUniqueId()).removePlayer();
        this.playersInChairs.remove(player.getUniqueId());
    }

    public boolean inChair(Player player){
        return playersInChairs.containsKey(player.getUniqueId());
    }

    public HashSet<Chair> getChairsList() {
        return chairsList;
    }

    public void setChairsList(HashSet<Chair> chairsList) {
        this.chairsList = chairsList;
    }

    public HashSet<Location> getChairLocations() {
        return chairLocations;
    }

    public void setChairLocations(HashSet<Location> chairLocations) {
        this.chairLocations = chairLocations;
    }

    public HashMap<UUID, Chair> getPlayersInChairs() {
        return playersInChairs;
    }

    public void setPlayersInChairs(HashMap<UUID, Chair> playersInChairs) {
        this.playersInChairs = playersInChairs;
    }

    public int getTaskInt() {
        return taskInt;
    }
}
