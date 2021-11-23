package me.aov.objects;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.aov.AfkFishing;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.EquipmentSlot;
import me.aov.Util.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Chair {

    private AfkFishing main;

    private int timeLeft; //Time for rewards
    private ChairDescription chairDescription;
    private Player sittingPlayer;
    private ArmorStand armorStand;
    private Hologram chairHologram;
    private Location armorStandLocation;
    private Location hologramLocation;
    private Location chairLocation;

    public Chair(AfkFishing main, Location chairLocation, ChairDescription chairDescription) {
        this.main = main;
        this.chairLocation = chairLocation;
        this.chairDescription = chairDescription;
        this.hologramLocation = chairLocation.clone().add(.5, chairDescription.getHologramOffset(), .5);
        this.armorStandLocation = chairLocation.clone().add(.5, chairDescription.getChairOffset(), .5);
        this.sittingPlayer = null;
        this.timeLeft = chairDescription.getTime();
        updateHologram();
        setUpChair();
    }

    public Chair(AfkFishing main, ChairDescription chairDescription) {
        this.main = main;
        this.chairDescription = chairDescription;
    }

    public void count() {
        if (sittingPlayer != null) {
            if (timeLeft > 0) {
                timeLeft--;
            } else {
                timeLeft = chairDescription.getTime();
                if (main.getConfig().getBoolean("require-fishing-rod")) {
                    if (sittingPlayer.getInventory().getItemInMainHand().getType().equals(Material.FISHING_ROD)) {
                        chairDescription.getRewardTable().getReward(sittingPlayer, this);
                        if (main.getConfig().getBoolean("damage-rod")) {
                            damageRod();
                        }
                    } else {
                        sittingPlayer.sendMessage(Color.color(main.getDataManager().getLang("no-rod-message")));
                    }
                } else {
                    chairDescription.getRewardTable().getReward(sittingPlayer, this);
                }
            }
            updateHologram();
        }
    }

    public void updateHologram() {
        if (chairHologram == null) {
            chairHologram = HologramsAPI.createHologram(main, hologramLocation);
        } else if (chairHologram.getLocation() != hologramLocation) {
            chairHologram.teleport(hologramLocation);
        }
        chairHologram.clearLines();
        for (int i = 0; i < chairDescription.getHologramLines().size(); i++) {
            String user = main.getDataManager().getLang("empty-user");
            if (sittingPlayer != null) {
                user = sittingPlayer.getDisplayName();
            }
            chairHologram.appendTextLine(Color.color(chairDescription.getHologramLines().get(i)
                    .replaceAll("%display%", chairDescription.getDisplay())
                    .replaceAll("%timeleft%", timeLeft + "")
                    .replaceAll("%totaltime%", chairDescription.getTime() + "")
                    .replaceAll("%user%", user)));
        }
        chairHologram.getVisibilityManager().setVisibleByDefault(true);

    }

    private void setUpChair() {
        armorStand = (ArmorStand) chairLocation.getWorld().spawnEntity(armorStandLocation, EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setMarker(true);
        armorStand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.ADDING_OR_CHANGING);
        armorStand.setAI(false);
        armorStand.setSmall(true);
        armorStand.setCanPickupItems(false);
        armorStand.setGravity(false);
        armorStand.setInvulnerable(true);
    }

    public void sit(Player player) {
        armorStand.addPassenger(player);
        sittingPlayer = player;
        updateHologram();
    }

    public void removePlayer() {
        sittingPlayer = null;
        if (main.getConfig().getBoolean("reset-time-on-dismount")) {
            timeLeft = chairDescription.getTime();
        }
        updateHologram();
    }

    public void damageRod() {
        ItemStack itemStack = sittingPlayer.getInventory().getItemInMainHand();
        int unbreakingLevel = itemStack.getEnchantmentLevel(Enchantment.DURABILITY);
        double chance = 100D / (unbreakingLevel + 1);
        Damageable damageable = (Damageable) itemStack.getItemMeta();
        Random random = new Random();
        if (random.nextDouble() * 100 < chance) {
            damageable.setDamage(damageable.getDamage() + 1);
            itemStack.setItemMeta((ItemMeta) damageable);
            System.out.println(damageable.getDamage());
            System.out.println("max " + itemStack.getType().getMaxDurability());
            if (damageable.getDamage() > itemStack.getType().getMaxDurability()) {
                Bukkit.getPluginManager().callEvent(new PlayerItemBreakEvent(sittingPlayer, itemStack));
                itemStack.setAmount(0);
                sittingPlayer.playSound(sittingPlayer.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                sittingPlayer.sendMessage(Color.color(main.getDataManager().getLang("prefix") + main.getDataManager().getLang("rod-broken")));
            }
        }

    }

    public ChairSerialization getChairSerialization() {
        return new ChairSerialization(chairLocation, chairDescription.getName());
    }

    public AfkFishing getMain() {
        return main;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public ChairDescription getChairDescription() {
        return chairDescription;
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public Hologram getChairHologram() {
        return chairHologram;
    }

    public Location getArmorStandLocation() {
        return armorStandLocation;
    }

    public Location getHologramLocation() {
        return hologramLocation;
    }

    public Location getChairLocation() {
        return chairLocation;
    }

    public void setHologramLocation(Location hologramLocation) {
        this.hologramLocation = hologramLocation;
    }

    public Player getSittingPlayer() {
        return sittingPlayer;
    }
}
