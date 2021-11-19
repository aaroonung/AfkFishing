package me.aov.commands;

import me.aov.AfkFishing;
import me.aov.objects.Chair;
import me.aov.objects.RewardTable;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FishCommands implements CommandExecutor {

    private AfkFishing main;
    public FishCommands(AfkFishing main){
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            main.getDataManager().rewardTableSet.get("default rewards table").sendFullInventory(player);
        }
        return true;
    }
}
