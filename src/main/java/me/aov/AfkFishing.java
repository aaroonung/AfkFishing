package me.aov;

import me.aov.commands.FishCommands;
import me.aov.listeners.ChairClickListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public class AfkFishing extends JavaPlugin {

    private DataManager dataManager;

    @Override
    public void onEnable() {
        checkDepends();
        this.getCommand("fish").setExecutor(new FishCommands(this));
        getServer().getPluginManager().registerEvents(new ChairClickListener(this), this);
        super.onEnable();
        dataManager = new DataManager(this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private void checkDepends() {
        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            getLogger().severe("*** HolographicDisplays is not installed or not enabled. ***");
            getLogger().severe("*** This plugin will be disabled. ***");
            this.setEnabled(false);
            return;
        }
    }

    public DataManager getDataManager() {
        return dataManager;
    }

}

//TODO Add Chair Right Click
//TODO Add Right Click rewards menu
//TODO Add admin right click menu
//TODO Add chair creation
//TODO Add reload
//TODO Add permissions, commands
