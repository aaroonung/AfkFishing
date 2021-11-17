package me.aov.objects;

import me.aov.AfkFishing;
import me.aov.Util.Color;
import me.aov.Util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class RewardTable {

    private final NavigableMap<Double, String> map = new TreeMap<Double, String>();
    private final Random random;
    private double total = 0;
    private final AfkFishing main;

    public RewardTable(AfkFishing main) {
        this.random = new Random();
        this.main = main;
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
        String[] reward = map.higherEntry(value).getValue().split("\\|\\|");
        for(String stringValue : reward){
            if(stringValue.startsWith("item(")){
            }
        }





    }

    /*"Dirt|0|Amount|Name|Lore|Enchants:level Enchants |
    fields[0] = material
    fields[1] = durability
    fields[2] = amount
    fields[3] = display name
    fields[4] = lore\nlore2
    fields[5] = enchant:level
    fields[6] = effects:level:duration
     */
    public ItemStack parseItemStack(String target) {
        String[] fields = target.split("\\|");
        Material material = Material.getMaterial(fields[0]);
        int durability = 0;
        int amount = 0;
        String name = "";
        try {
            durability = Integer.parseInt(fields[1]);
        } catch (Exception e) {
            main.getLogger().warning("Error at durability in " + target);
            return null;
        }
        try {
            amount = Integer.parseInt(fields[2]);
        } catch (Exception e) {
            main.getLogger().warning("Error at amount in " + target);
            return null;
        }

        if (!fields[3].isBlank()) {
            name = Color.color(fields[3]);
        }

        List<String> lore = null;
        if (!fields[4].isBlank()) {
            lore = Arrays.asList(Color.color(fields[4]).split("\\n"));
        }

        if (material == null || amount == 0) {
            main.getLogger().warning("Error at material or amount in " + target);
            return null;
        }
        ItemBuilder itemBuilder = new ItemBuilder(material);
        if (lore != null) {
            itemBuilder.setLore(lore);
        }
        itemBuilder.setName(name).setDurability((short) durability);

        //Enchantments
        for (String s : fields[5].split(" ")) {
            if (!s.isBlank()) {
                int level = 0;
                String[] x = s.split(":");
                Enchantment enchantments = Enchantment.getByKey(NamespacedKey.minecraft(x[0]));
                try {
                    level = Integer.parseInt(x[1]);
                } catch (Exception e) {
                    main.getLogger().warning("Incorrect level value at" + s + " in " + target);
                    continue;
                }
                if (enchantments != null) {
                    itemBuilder.addEnchant(enchantments, level);
                }
            }
        }
        ItemStack itemStack = itemBuilder.toItemStack();
        //POTIONS
        for (String p : fields[6].split(" ")) {
            if (!p.isBlank()) {
                int level = 0;
                int duration = 0;
                String[] s = p.split(":");
                PotionEffectType potionType = PotionEffectType.getByName(s[0].toLowerCase());
                if (potionType == null) {
                    main.getLogger().warning("Incorrect potion type at " + target);
                    continue;
                }
                try {
                    level = Integer.parseInt(s[1]);
                    duration = Integer.parseInt(s[2]);
                } catch (Exception e) {
                    main.getLogger().warning("Incorrect level value at" + s + " in " + target);
                    continue;
                }
                PotionEffect potion = new PotionEffect(potionType, duration*20, level);
                if (itemStack.getType().equals(Material.POTION) || itemStack.getType().equals(Material.SPLASH_POTION) || itemStack.getType().equals(Material.LINGERING_POTION)) {
                    PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
                    meta.addCustomEffect(potion, false);
                    itemStack.setItemMeta(meta);
                }
            }
        }
        itemStack.setAmount(amount);
        return itemStack;
    }


    public String getItemString(ItemStack itemStack) {
        StringBuilder sb = new StringBuilder();
        sb.append("item(");
        sb.append(itemStack.getType().toString())
                .append("|")
                .append(((Damageable) itemStack.getItemMeta()).getDamage())
                .append("|")
                .append(itemStack.getAmount())
                .append("|");
        if (itemStack.hasItemMeta()) {
            if (itemStack.getItemMeta().hasDisplayName()) {
                sb.append(itemStack.getItemMeta().getDisplayName());
            }
            sb.append("|");
            if (itemStack.getItemMeta().hasLore()) {
                for (String s : itemStack.getItemMeta().getLore()) {
                    sb.append(s + "\n");
                }
                sb.delete(sb.length() - 2, sb.length());
            }
            sb.append("|");
            if (itemStack.getItemMeta().hasEnchants()) {
                for (Enchantment e : itemStack.getItemMeta().getEnchants().keySet()) {
                    sb.append(e.getKey().getKey() + ":" + itemStack.getItemMeta().getEnchants().get(e) + " ");
                }
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append("|");
            PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
            if (itemStack.getType().equals(Material.POTION) || itemStack.getType().equals(Material.SPLASH_POTION) || itemStack.getType().equals(Material.LINGERING_POTION)) {
                if (potionMeta.hasCustomEffects()) {
                    for (PotionEffect potionEffect : potionMeta.getCustomEffects()) {
                        sb.append(potionEffect.getType().getName() + ":" + potionEffect.getAmplifier() + ":" + potionEffect.getDuration() + " ");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                }
            }
            sb.append("|");
        } else {
            sb.append("|").append("|").append("|").append("|");
        }
        sb.append(")");
        return sb.toString();
    }

    //TODO Add ItemStack in hand > String
    //TODO Get String ItemStack
    //TODO Add ItemFlags
    //TODO Add msg, broadcast, cmd


}
