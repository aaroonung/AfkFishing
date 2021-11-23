package me.aov.listeners;

import me.aov.AfkFishing;
import me.aov.Util.Color;
import me.aov.objects.Chair;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class MenuListeners implements Listener {

    private AfkFishing main;

    public MenuListeners(AfkFishing main) {
        this.main = main;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event != null) {
            if (event.getWhoClicked().getOpenInventory().getTitle().equals(Color.color(main.getDataManager().getMenuLangConfiguration().getString("menu-title")))) {
                event.setResult(Event.Result.DENY);
            } else if (event.getWhoClicked().getOpenInventory().getTitle().equals(Color.color(main.getDataManager().getMenuLangConfiguration().getString("admin-menu-title")))) {
                if (event.getClickedInventory() != null && event.getCurrentItem() != null && isAfkItem(event.getCurrentItem())) {
                    if (typeOfAfkItem(event.getCurrentItem()).equalsIgnoreCase("raise")) {
                        main.getDataManager().getChairDescriptions()
                                .get(event.getInventory().getItem(0)
                                        .getItemMeta().getDisplayName()
                                        .replace("Chair Description: ", ""))
                                .moveHologram(true);
                    } else if (typeOfAfkItem(event.getCurrentItem()).equalsIgnoreCase("lower")) {
                        main.getDataManager().getChairDescriptions()
                                .get(event.getInventory().getItem(0)
                                        .getItemMeta().getDisplayName()
                                        .replace("Chair Description: ", ""))
                                .moveHologram(false);
                    } else if (typeOfAfkItem(event.getCurrentItem()).equalsIgnoreCase("delete")) {
                        main.getChairManager().removeChair(getChairFromMenu(event.getClickedInventory()));
                        event.getWhoClicked().closeInventory();
                        event.getWhoClicked().sendMessage(Color.color(main.getDataManager().getLang("chair-deleted-message")));
                    }
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getPlayer().getOpenInventory().getTitle().equals(Color.color(main.getDataManager().getMenuLangConfiguration().getString("admin-menu-title")))) {
            ArrayList<ItemStack> itemStacks = new ArrayList<>();
            for (ItemStack i : event.getInventory().getContents()) {
                if (i != null && !isAfkItem(i)) {
                    itemStacks.add(i);
                }
            }
            main.getDataManager().saveItemStacks(itemStacks, event.getInventory().getItem(0).getItemMeta().getDisplayName().replace("Chair Description: ", ""));
        }
    }

    private boolean isAfkItem(ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) {
            return false;
        }
        return itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(main, "afkfishing"), PersistentDataType.BYTE);
    }

    private String typeOfAfkItem(ItemStack itemStack) {
        Byte b = itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "afkfishing"), PersistentDataType.BYTE);
        if (b.toString().equalsIgnoreCase("1")) {
            return "filler";
        } else if (b.toString().equalsIgnoreCase("2")) {
            return "raise";
        } else if (b.toString().equalsIgnoreCase("3")) {
            return "lower";
        } else if (b.toString().equalsIgnoreCase("4")) {
            return "delete";
        }
        return null;
    }

    private Chair getChairFromMenu(Inventory inventory){
        ItemStack itemStack = inventory.getItem(1);
        int blockX = Integer.parseInt(itemStack.getItemMeta().getLore().get(0));
        int blockY = Integer.parseInt(itemStack.getItemMeta().getLore().get(1));
        int blockZ = Integer.parseInt(itemStack.getItemMeta().getLore().get(2));
        String world = itemStack.getItemMeta().getLore().get(3);
        for(Chair ch : main.getChairManager().getChairsList()){
            if(blockX == ch.getChairLocation().getBlockX() && blockY == ch.getChairLocation().getBlockY() && blockZ == ch.getChairLocation().getBlockZ()){
                return ch;
            }
        }
        return null;
    }


}
