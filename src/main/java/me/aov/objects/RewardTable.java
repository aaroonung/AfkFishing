package me.aov.objects;

import me.aov.Util.Color;
import me.aov.Util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class RewardTable {

    private final NavigableMap<Double, String> map = new TreeMap<Double, String>();
    private final Random random;
    private double total = 0;

    public RewardTable() {
        this.random = new Random();
    }

    public RewardTable(Random random) {
        this.random = random;
    }

    public RewardTable add(double weight, String result) {
        if (weight <= 0) {
            return this;
        }
        total += weight;
        map.put(total, result);
        return this;
    }

    public void getReward(Player player) {
        double value = random.nextDouble() * total;
        String[] reward = map.higherEntry(value).getValue().split("||");
    }

    /*"Dirt|0|Amount|Name|Lore|Enchants:level Enchants |
    fields[0] = material
    fields[1] = durability
    fields[2] = amount
    fields[3] = display name
    fields[4] = lore\nlore2
    fields[5] = enchant:level
    fields[6] = effects

     */
    public static ItemStack parseItemStack(String target) {
        String[] fields = target.split("\\|");
        Material material = Material.getMaterial(fields[0]);
        int durability = 0;
        int amount = 0;
        String name = "";
        try {
            durability = Integer.parseInt(fields[1]);
        } catch (Exception e) {
            return null;
        }
        try {
            amount = Integer.parseInt(fields[2]);
        } catch (Exception e) {
            return null;
        }

        if(!fields[3].isBlank()){
            name = Color.color(fields[3]);
        }

        List<String> lore = null;
        if(!fields[4].isBlank()){
            lore = Arrays.asList(Color.color(fields[4]).split("\\n"));
        }

        if (material == null || amount == 0) {
            return null;
        }

        ItemBuilder itemBuilder = new ItemBuilder(material);
        if(lore != null){
            itemBuilder.setLore(lore);
        }
        itemBuilder.setName(name).setDurability((short) durability);


        //Enchantments
        for(String s : fields[5].split(" ")){
            int level = 0;
            String[] x = s.split(":");
            Enchantment enchantments = Enchantment.getByKey(NamespacedKey.minecraft(x[1]));
            try{
                level = Integer.parseInt(x[2]);
            }catch(Exception e){
                break;
            }
            if(enchantments != null){
                itemBuilder.addEnchant(enchantments, level);
            }
        }
        //TODO Add Potion Parsing


        ItemStack itemStack = itemBuilder.toItemStack();
        itemStack.setAmount(amount);

        return itemStack;
    }

    //TODO Add ItemStack in hand > String
    //TODO Get String ItemStack
    //TODO Add ItemFlags
    //TODO Add msg, broadcast, cmd


}
