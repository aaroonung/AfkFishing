package me.aov.listeners;

import me.aov.AfkFishing;
import me.aov.Util.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class MenuListeners implements Listener {

    private AfkFishing main;

    public MenuListeners(AfkFishing main) {
        this.main = main;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        if(event != null){
            if(event.getWhoClicked().getOpenInventory().getTitle().equals(Color.color(main.getDataManager().getMenuLangConfiguration().getString("title")))){
                event.setResult(Event.Result.DENY);
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        if(event.getPlayer().getOpenInventory().getTitle().equals(Color.color(main.getDataManager().getMenuLangConfiguration().getString("title")))){
            ArrayList<ItemStack> itemStacks = new ArrayList<>();
            for(ItemStack i : event.getInventory().getContents()){
                if(!isAfkItem(i)){
                    itemStacks.add(i);
                }
            }
            main.getDataManager().saveItemStacks(itemStacks, event.getInventory().getItem(0).getItemMeta().getDisplayName().replace("Chair Description:", ""));
        }
    }

    private boolean isAfkItem(ItemStack itemStack){
        if(itemStack.hasItemMeta()){
            return false;
        }
        return itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(main, "afkfishing"), PersistentDataType.BYTE);
    }

}
