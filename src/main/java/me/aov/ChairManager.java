package me.aov;

import me.aov.objects.Chair;
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

    public ChairManager(AfkFishing main) {
        this.main = main;
        chairsList = new HashSet<>();
        chairLocations = new HashSet<>();
        playersInChairs = new HashMap<>();
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
    }

    public void sitInChair(Player player, Location location){
        Chair ch = getChairAt(location);
        if(isChair(location) && ch != null){
            playersInChairs.put(player.getUniqueId(), ch);
            ch.sit(player);
        }
    }

    public boolean inChair(Player player){
        return playersInChairs.containsKey(player.getUniqueId());
    }

}
