package me.aov.listeners;

import me.aov.AfkFishing;
import me.aov.Util.Color;
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

        if (event != null && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (event.getClickedBlock() != null
                    && !main.getChairManager().inChair(event.getPlayer())) {
                Player player = event.getPlayer();
                if (main.getChairManager().getCreatingChair().containsKey(player.getUniqueId()) && !main.getChairManager().isChair(event.getClickedBlock().getLocation())) {
                    if (player.isSneaking()) {
                        Chair ch = new Chair(main, event.getClickedBlock().getLocation(), main.getDataManager().getChairDescriptions().get("test desc"));
                        main.getChairManager().getChairsList().add(ch);
                        main.getChairManager().getChairLocations().add(event.getClickedBlock().getLocation());
                        main.getChairManager().getCreatingChair().remove(player.getUniqueId());
                        player.sendMessage(Color.color(main.getDataManager().getLang("prefix") + main.getDataManager().getLang("chair-create-message")));
                    }
                }
                if (main.getChairManager().isChair(event.getClickedBlock().getLocation())) {
                    if (player.isSneaking()) {
                        main.getMenuManager().openMenu(player, main.getChairManager().getChairAt(event.getClickedBlock().getLocation()));

                    } else {
                        if (!main.getChairManager().getChairAt(event.getClickedBlock().getLocation()).getChairDescription().getPermission().isBlank()) {
                            if (player.hasPermission(main.getChairManager().getChairAt(event.getClickedBlock().getLocation()).getChairDescription().getPermission())) {
                                main.getChairManager().sitInChair(player, event.getClickedBlock().getLocation());
                            }else{
                                player.sendMessage(Color.color(main.getDataManager().getLang("prefix")
                                        + main.getDataManager().getLang("no-permission-message")));
                            }
                        } else {
                            main.getChairManager().sitInChair(player, event.getClickedBlock().getLocation());
                        }
                    }
                }
            }
        }

    }
}
