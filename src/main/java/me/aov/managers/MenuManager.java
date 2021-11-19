package me.aov.managers;

import me.aov.AfkFishing;
import me.aov.Util.Color;
import me.aov.Util.ItemBuilder;
import me.aov.objects.Chair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class MenuManager {
    private AfkFishing main;

    public MenuManager(AfkFishing main) {
        this.main = main;
    }

    public void openMenu(Player player, Chair chair) {
        if (player.hasPermission("afkfishing.admin") || player.isOp()) {
            player.openInventory(getAdminMenu(chair));
        } else {
            player.openInventory(getNormalMenu(chair));
        }
    }

    public Inventory getAdminMenu(Chair chair) {
        Inventory inventory = Bukkit.createInventory(null, 54, Color.color(main.getDataManager().getMenuLangConfiguration().getString("admin-menu-title")));
        inventory.setContents(getNormalMenu(chair).getContents().clone());
        ItemBuilder itemBuilder = new ItemBuilder(Material.OAK_SIGN);
        itemBuilder.setName("Chair Description: " + chair.getChairDescription().getName());
        ItemStack nameIndicator = itemBuilder.toItemStack();
        setAfkItem(nameIndicator);
        inventory.setItem(0, nameIndicator);
        return inventory;
    }

    public Inventory getNormalMenu(Chair chair) {
        Inventory inventory = Bukkit.createInventory(null, 54, Color.color(main.getDataManager().getMenuLangConfiguration().getString("menu-title")));
        setBorderItem(inventory);
        if (chair.getChairDescription().getItemStacks() != null) {
            for (ItemStack itemStack : chair.getChairDescription().getItemStacks()) {
                inventory.addItem(itemStack);
            }
        }
        return inventory;
    }

    public void setBorderItem(Inventory menu) {
        ItemBuilder border = new ItemBuilder(Material.valueOf(main.getDataManager().getMenuLangConfiguration().getString("border-item-material")));
        ItemStack itemStack = border.setName(Color.color(main.getDataManager().getMenuLangConfiguration().getString("border-item-title"))).toItemStack();
        setAfkItem(itemStack);
        int i;
        for (i = menu.getSize() - 9; i < menu.getSize(); i++) {
            menu.setItem(i, itemStack);
        }
        for (i = 0; i < menu.getSize(); i += 9) {
            menu.setItem(i, itemStack);
        }
        for (i = 8; i < menu.getSize(); i += 9) {
            menu.setItem(i, itemStack);
        }
        for (i = 0; i < 9; i++) {
            menu.setItem(i, itemStack);
        }
    }

    public void setAfkItem(ItemStack itemStack){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(main, "afkfishing"), PersistentDataType.BYTE, Byte.valueOf("1"));
        itemStack.setItemMeta(itemMeta);
    }
}
