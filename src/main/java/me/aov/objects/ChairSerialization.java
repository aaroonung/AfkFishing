package me.aov.objects;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.LinkedHashMap;
import java.util.Map;

@SerializableAs("ChairSerialization")
public class ChairSerialization implements ConfigurationSerializable {

    private Location chairLocation;
    private String chairDescription;

    public ChairSerialization(Location chairLocation, String chairDescription) {
        this.chairLocation = chairLocation;
        this.chairDescription = chairDescription;
    }

    public Location getChairLocation() {
        return chairLocation;
    }

    public void setChairLocation(Location chairLocation) {
        this.chairLocation = chairLocation;
    }

    public String getChairDescription() {
        return chairDescription;
    }

    public void setChairDescription(String chairDescription) {
        this.chairDescription = chairDescription;
    }

    @Override
    public Map<String, Object> serialize() {
        LinkedHashMap result = new LinkedHashMap();
        result.put("chairLocation", chairLocation);
        result.put("chairDescription", chairDescription);
        return result;
    }

    public static ChairSerialization deserialize(Map<String, Object> args){
        Location l = (Location) args.get("chairLocation");
        String s = (String) args.get("chairDescription");
        return new ChairSerialization(l, s);
    }


}
