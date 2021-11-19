package me.aov.listeners;

import me.aov.AfkFishing;
import me.aov.objects.Chair;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChairClickListener implements Listener {
    private AfkFishing main;

    public ChairClickListener(AfkFishing main) {
        this.main = main;
    }

    @EventHandler(ignoreCancelled = false)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event != null && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            Player player = event.getPlayer();
        }

    }
}
