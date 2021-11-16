package me.aov.objects;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import me.aov.AfkFishing;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

public class Chair {

    private AfkFishing main;

    private int time; //Time for rewards
    private int id; //Instance Specific Chairs
    private ChairDescription chairDescription;
    private ArmorStand armorStand;
    private Hologram chairHologram;
    private Location armorStandLocation;
    private Location hologramLocation;
    private Location chairLocation;


    public Chair(AfkFishing main, Location chairLocation){
        this.main = main;
        this.chairLocation = chairLocation;
        this.hologramLocation = chairLocation.clone().add(.5,4,.5);
        this.armorStandLocation = chairLocation.clone().add(.5,.25,.5);
        setUpHologram();
        setUpChair();
    }

    private void setUpHologram(){
        chairHologram = HologramsAPI.createHologram(main, hologramLocation);
        TextLine textLine1 = chairHologram.appendTextLine("Fishing Chair");

        chairHologram.getVisibilityManager().setVisibleByDefault(true);
    }

    private void setUpChair(){
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

    public void sit(Player player){
        armorStand.addPassenger(player);
    }



}
