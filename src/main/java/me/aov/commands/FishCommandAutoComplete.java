package me.aov.commands;

import me.aov.AfkFishing;
import me.aov.objects.ChairDescription;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FishCommandAutoComplete implements TabCompleter {

    private final AfkFishing main;

    public FishCommandAutoComplete(AfkFishing main) {
        this.main = main;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> stuff = new ArrayList<>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                if (sender.hasPermission("afkfishing.create")) {
                    stuff.add("create");
                }
                if (sender.hasPermission("afkfishing.list")) {
                    stuff.add("list");
                }
                if (sender.hasPermission("afkfishing.help")) {
                    stuff.add("help");
                }
                if (sender.hasPermission("afkfishing.get")) {
                    stuff.add("get");
                }
                return stuff;
            } else if (args.length > 1) {
                if (args[0].equalsIgnoreCase("create") && (sender.hasPermission("afkfishing.create") || sender.isOp())) {
                    stuff.addAll(main.getDataManager().getChairDescriptions().keySet());
                    return stuff;
                } else if (args[0].equalsIgnoreCase("list") && (sender.hasPermission("afkfishing.list")|| sender.isOp())) {
                    stuff.add("chairs");
                    stuff.add("descriptions");
                    stuff.add("rewards");
                    return stuff;
                } else if (args[0].equalsIgnoreCase("get") && (sender.hasPermission("afkfishing.get")|| sender.isOp())) {
                    if (args.length > 2) {
                        if (args[1].equalsIgnoreCase("configline")) {
                            return stuff;
                        } else if (args[1].equalsIgnoreCase("reward")) {
                            stuff.addAll(main.getDataManager().getRewardTableSet().keySet());
                            return stuff;
                        } else if (args[1].equalsIgnoreCase("item")) {
                            return stuff;
                        }
                    }
                    stuff.add("configLine");
                    stuff.add("reward");
                    stuff.add("item");
                }
            }
        }


        return stuff;
    }
}
