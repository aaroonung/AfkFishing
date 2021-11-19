package me.aov.listeners;

import me.aov.AfkFishing;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

public class ChairRemovalListeners implements Listener {

    private AfkFishing main;

    public ChairRemovalListeners(AfkFishing main) {
        this.main = main;
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = ((Player) event.getEntity()).getPlayer();
            assert player != null;
            if (main.getChairManager().inChair(player)) {
                main.getChairManager().removePlayer(player);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (main.getChairManager().inChair(event.getPlayer())) {
            main.getChairManager().getPlayersInChairs().get(event.getPlayer().getUniqueId()).getArmorStand().removePassenger(event.getPlayer());
            main.getChairManager().removePlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (main.getChairManager().inChair(event.getPlayer())) {
            if (main.getChairManager()
                    .getPlayersInChairs()
                    .get(event.getPlayer()
                            .getUniqueId()).getArmorStand()
                    .getPassengers().contains(event.getPlayer())) {
                main.getChairManager().getPlayersInChairs().get(event.getPlayer().getUniqueId()).getArmorStand().removePassenger(event.getPlayer());
            }
            main.getChairManager().removePlayer(event.getPlayer());
        }
    }


}
