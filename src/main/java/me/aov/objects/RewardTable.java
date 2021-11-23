package me.aov.objects;

import me.aov.AfkFishing;
import me.aov.Util.Color;
import me.aov.Util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
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
    private FileConfiguration configFile;

    public RewardTable(FileConfiguration config, AfkFishing main) {
        this.random = new Random();
        this.main = main;
        this.configFile = config;
        for (String s : config.getStringList("table")) {
            String[] temp = s.split(";");
            double weight = 0;
            try {
                weight = Double.parseDouble(temp[0]);
            } catch (NumberFormatException exception) {
                main.getLogger().warning("Probability error at: " + s);
                continue;
            }
            add(weight, s);
        }
    }

    public RewardTable add(double weight, String result) {
        if (weight <= 0) {
            return this;
        }
        total += weight;
        map.put(total, result);
        return this;
    }

    public void getReward(Player player, Chair chair) {
        double value = random.nextDouble() * total;
        String[] reward = map.higherEntry(value).getValue().split(";");
        boolean rewardFullInventory = main.getConfig().getBoolean("reward-full-inventory");
        boolean inventoryFull = player.getInventory().firstEmpty() == -1;
        if(!rewardFullInventory && inventoryFull){
            sendFullInventory(player);
            return;
        }
        for(String s : reward){
            if(s.startsWith("permission(") && !player.hasPermission(s.substring(s.indexOf("(") + 1, s.indexOf(")")))){
                getReward(player, chair);
                return;
            }
        }
        for (String stringValue : reward) {
            if (!stringValue.contains("(")) {
                continue;
            }
            String substring = stringValue.substring(stringValue.indexOf("(") + 1, stringValue.indexOf(")"));
            if (stringValue.startsWith("item(")) {
                giveItem(player, parseItemStack(substring));
            } else if (stringValue.startsWith("cmd(")) {
                main.getServer().dispatchCommand(main.getServer().getConsoleSender(), substring
                        .replaceAll("%player%", player.getDisplayName())
                        .replaceAll("%chair name%", chair.getChairDescription().getDisplay()));
            } else if (stringValue.startsWith("broadcast(")) {
                main.getServer().broadcastMessage(Color.color(main.getDataManager().getLang("prefix") + substring)
                        .replaceAll("%player%", player.getDisplayName())
                        .replaceAll("%chair name%", chair.getChairDescription().getDisplay()));
            } else if(stringValue.startsWith("msg(")){
                player.sendMessage(Color.color(main.getDataManager().getLang("prefix") + substring));
            }

        }
        if (!main.getDataManager().getLang("reward-message").isBlank()) {
            player.sendMessage(Color.color(main.getDataManager().getLang("prefix")
                    + main.getDataManager().getLang("reward-message")));
        }
    }

    public void sendFullInventory(Player player){
        player.sendTitle(Color.color(main.getDataManager().getLang("full-inventory-title")),
                Color.color(main.getDataManager().getLang("full-inventory-subtitle")),
                main.getDataManager().getLangConfiguration().getInt("full-inventory-fade-in"),
                main.getDataManager().getLangConfiguration().getInt("full-inventory-stay"),
                main.getDataManager().getLangConfiguration().getInt("full-inventory-fade-out"));
    }

    public void giveItem(Player player, ItemStack itemStack) {
       if(player.getInventory().firstEmpty() != -1){
           player.getInventory().setItem(player.getInventory().firstEmpty(), itemStack);
       }else{
           if(main.getConfig().getBoolean("drop-items-on-full")){
               player.getWorld().dropItem(player.getLocation(), itemStack);
           }
       }
    }

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
        if (!fields[4].trim().isBlank()) {
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
        try {
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
        } catch (IndexOutOfBoundsException ignored) {
        }
        ItemStack itemStack = itemBuilder.toItemStack();
        //POTIONS
        try {
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
                    PotionEffect potion = new PotionEffect(potionType, duration * 20, level);
                    if (itemStack.getType().equals(Material.POTION) || itemStack.getType().equals(Material.SPLASH_POTION) || itemStack.getType().equals(Material.LINGERING_POTION)) {
                        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
                        meta.addCustomEffect(potion, false);
                        itemStack.setItemMeta(meta);
                    }
                }
            }
        } catch (IndexOutOfBoundsException ignored) {
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


}
