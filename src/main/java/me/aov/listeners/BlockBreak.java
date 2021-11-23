package me.aov.listeners;

import me.aov.AfkFishing;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {

    private AfkFishing main;

    public BlockBreak(AfkFishing main){
        this.main = main;
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if(main.getChairManager().getChairLocations().contains(event.getBlock().getLocation())){
                event.setCancelled(true);
                event.setDropItems(false);
                event.setExpToDrop(0);
        }
    }



}
