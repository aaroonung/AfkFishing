package me.aov.commands;

import me.aov.AfkFishing;
import me.aov.Util.Center;
import me.aov.Util.Color;
import me.aov.objects.Chair;
import me.aov.objects.ChairDescription;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class FishCommands implements CommandExecutor {

    private AfkFishing main;

    public FishCommands(AfkFishing main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length >= 2) {
                if (args[0].equalsIgnoreCase("create")
                        && (sender.hasPermission("afkfishing.create") || sender.isOp())) {
                    create(player, cmd, args);
                    return true;
                } else if (args[0].equalsIgnoreCase("list")
                        && (sender.hasPermission("afkfishing.list")) || sender.isOp()) {
                    list(player, args);
                    return true;
                } else if (args[0].equalsIgnoreCase("get")
                        && (sender.hasPermission("afkfishing.get") || sender.isOp())) {
                    get(player, args);
                    return true;
                }
            }
            help(player);
        }
        return true;
    }

    private void create(Player player, Command cmd, String[] args) {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            builder.append(args[i] + " ");
        }

        if (main.getDataManager().getChairDescriptions().containsKey(builder.toString().trim())) {
            main.getChairManager().creatingChair(player, builder.toString().trim());
        } else {
            player.sendMessage(me.aov.Util.Color.color(main.getDataManager().getLang("prefix")
                    + main.getDataManager().getLang("desc-not-found")));
        }
    }

    private void list(Player player, String[] args) {
        if (args[1].equalsIgnoreCase("chairs")) {
            player.sendMessage(Color.color(main.getDataManager().getLang("prefix")
                    + "List of Active Chairs: "));
            for (Chair ch : main.getChairManager().getChairsList()) {
                player.sendMessage(Color.color(main.getDataManager().getLang("prefix")
                        + ch.getChairDescription().getDisplay() + ": &r"
                        + ch.getChairLocation().getWorld().getName() + "("
                        + ch.getChairDescription().getName() + "): "
                        + ch.getChairLocation().getBlockX() + ", " + ch.getChairLocation().getBlockY() + ", "
                        + ch.getChairLocation().getBlockZ() + ", "));
            }
        } else if (args[1].equalsIgnoreCase("descriptions")) {
            player.sendMessage(Color.color(main.getDataManager().getLang("prefix")
                    + "List of Valid Descriptions: "));
            for (String s : main.getDataManager().getChairDescriptions().keySet()) {
                player.sendMessage(Color.color(main.getDataManager().getLang("prefix")
                        + s));
            }
        } else if (args[1].equalsIgnoreCase("rewards")) {
            player.sendMessage(Color.color(main.getDataManager().getLang("prefix")
                    + "List of Reward Tables: "));
            for (String s : main.getDataManager().getRewardTableSet().keySet()) {
                player.sendMessage(Color.color(main.getDataManager().getLang("prefix")
                        + s));
            }
        } else {
            help(player);
        }
    }

    private void get(Player player, String[] args) {
        if (args[1].equalsIgnoreCase("configline")) {
            if (player.getInventory().getItemInMainHand() != null && !player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                player.sendMessage(Color.color(main.getDataManager().getLang("prefix") + "&7Also sent in console."));
                player.sendMessage(Color.color(main.getDataManager().getLang("prefix")
                        + main.getDataManager().getRewardTableSet().get("default rewards table")
                        .getItemString(player.getInventory().getItemInMainHand())));
                main.getServer().getConsoleSender().sendMessage(main.getDataManager().getRewardTableSet().get("default rewards table")
                        .getItemString(player.getInventory().getItemInMainHand()));
            } else {
                player.sendMessage(Color.color(main.getDataManager().getLang("prefix")
                        + "&7Hold an Item in your hand!"));
            }
        } else if (args[1].equalsIgnoreCase("reward") && args.length > 2) {
            StringBuilder builder = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                builder.append(args[i] + " ");
            }
            String s = builder.toString();
            for (String g : main.getDataManager().getRewardTableSet().keySet()) {
                System.out.println(g);
            }
            if (main.getDataManager().getRewardTableSet().containsKey(s.trim())) {
                Chair chair = new Chair(main, new ChairDescription(main, main.getDataManager().getRewardTableSet().get(s.trim())));
                chair.getChairDescription().getRewardTable().getReward(player, chair);
            } else {
                player.sendMessage(Color.color(main.getDataManager().getLang("prefix") + "&7Reward table not found "));
            }
        } else if (args[1].equalsIgnoreCase("item") && args.length > 2) {
            StringBuilder builder = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                builder.append(args[i] + " ");
            }
            String s = builder.toString();
            if (s.contains("item(")) {
                player.getWorld().dropItem(player.getLocation(),
                        main.getDataManager().getRewardTableSet().get("default rewards table").parseItemStack(s));
            } else {
                player.sendMessage(Color.color(main.getDataManager().getLang("prefix") + "&7Invalid format! Use item(....)"));
            }
        } else {
            help(player);
        }
    }

    private void help(Player player) {
        player.sendMessage(Center.CenteredMessage("&8===[&l&aAfk&bFishing&r&8]==="));
    }

}
