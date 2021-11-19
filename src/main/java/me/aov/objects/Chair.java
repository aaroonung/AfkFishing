package me.aov.objects;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.aov.AfkFishing;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import me.aov.Util.Color;

public class Chair {

    private AfkFishing main;

    private int timeLeft; //Time for rewards
    private int id; //Instance Specific Chairs
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

    public void count(){
        if(sittingPlayer != null) {
            if (timeLeft > 0) {
                timeLeft--;
            } else {
                timeLeft = chairDescription.getTime();
                chairDescription.getRewardTable().getReward(sittingPlayer, this);
            }
            updateHologram();
        }
    }

    private void updateHologram() {
        if (chairHologram == null) {
            chairHologram = HologramsAPI.createHologram(main, hologramLocation);
        }else if(chairHologram.getLocation() != hologramLocation){
            chairHologram.teleport(hologramLocation);
        }
        for (int i = 0; i < chairDescription.getHologramLines().size(); i++) {
            String user = main.getDataManager().getLang("empty-user");
            if(sittingPlayer != null){
                user = sittingPlayer.getDisplayName();
            }
            chairHologram.appendTextLine(Color.color(chairDescription.getHologramLines().get(i)
                    .replaceAll("%display%", chairDescription.getDisplay())
                    .replaceAll("%timeleft%", timeLeft + "")
                    .replaceAll("%totaltime%", chairDescription.getTime() +"")
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
        if(main.getConfig().getBoolean("reset-time-on-dismount")){
            timeLeft = chairDescription.getTime();
        }
        updateHologram();
    }

    public AfkFishing getMain() {
        return main;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public int getId() {
        return id;
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
}
