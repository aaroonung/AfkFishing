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

    private Inventory getAdminMenu(Chair chair) {
        Inventory inventory = Bukkit.createInventory(null, 54, Color.color(main.getDataManager().getMenuLangConfiguration().getString("admin-menu-title")));
        inventory.setContents(getNormalMenu(chair).getContents().clone());
        ItemBuilder itemBuilder = new ItemBuilder(Material.OAK_SIGN);
        itemBuilder.setName("Chair Description: " + chair.getChairDescription().getName());
        ItemStack nameIndicator = itemBuilder.toItemStack();
        setAfkItem(nameIndicator);
        inventory.setItem(0, nameIndicator);
        setAdminItems(inventory, chair);
        return inventory;
    }

    private Inventory getNormalMenu(Chair chair) {
        Inventory inventory = Bukkit.createInventory(null, 54, Color.color(main.getDataManager().getMenuLangConfiguration().getString("menu-title")));
        setBorderItem(inventory);
        if (chair.getChairDescription().getItemStacks() != null) {
            for (ItemStack itemStack : chair.getChairDescription().getItemStacks()) {
                inventory.addItem(itemStack);
            }
        }
        return inventory;
    }

    private void setBorderItem(Inventory menu) {
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

    private void setAdminItems(Inventory inventory, Chair chair){
        ItemBuilder itemBuilder = new ItemBuilder(Material.EMERALD_BLOCK);
        itemBuilder.setName(Color.color("&aRaise Hologram"));
        itemBuilder.setLore("Click to raise the hologram by .25 blocks");
        markAfkItem(itemBuilder.toItemStack(), "2");
        inventory.setItem(26, itemBuilder.toItemStack());

        itemBuilder = new ItemBuilder(Material.REDSTONE_BLOCK);
        itemBuilder.setName(Color.color("&cLower Hologram"));
        itemBuilder.setLore("Click to lower the hologram by .25 blocks");
        markAfkItem(itemBuilder.toItemStack(), "3");
        inventory.setItem(35, itemBuilder.toItemStack());

        itemBuilder = new ItemBuilder(Material.BARRIER);
        itemBuilder.setName(Color.color("&4&lDelete chair"));
        itemBuilder.setLore("Click to delete the chair");
        markAfkItem(itemBuilder.toItemStack(), "4");
        inventory.setItem(53, itemBuilder.toItemStack());

        itemBuilder = new ItemBuilder(Material.OAK_SIGN);
        itemBuilder.setName(Color.color("&7Chair Location"));
        itemBuilder.setLore(chair.getChairLocation().getBlockX()+"",
                chair.getChairLocation().getBlockY()+"",
                chair.getChairLocation().getBlockZ()+"",
                chair.getChairLocation().getWorld().getName());
        setAfkItem(itemBuilder.toItemStack());
        inventory.setItem(1,itemBuilder.toItemStack());
    }

    public void setAfkItem(ItemStack itemStack){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(main, "afkfishing"), PersistentDataType.BYTE, Byte.valueOf("1"));
        itemStack.setItemMeta(itemMeta);
    }

    public void markAfkItem(ItemStack itemStack, String s){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(main, "afkfishing"), PersistentDataType.BYTE, Byte.valueOf(s));
        itemStack.setItemMeta(itemMeta);
    }

}
