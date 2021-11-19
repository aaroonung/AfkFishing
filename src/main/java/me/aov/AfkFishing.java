package me.aov;

import me.aov.commands.FishCommands;
import me.aov.listeners.ChairClickListener;
import me.aov.listeners.ChairRemovalListeners;
import me.aov.managers.ChairManager;
import me.aov.managers.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public class AfkFishing extends JavaPlugin {

    private DataManager dataManager;
    private ChairManager chairManager;

    @Override
    public void onEnable() {
        checkDepends();
        this.getCommand("fish").setExecutor(new FishCommands(this));
        getServer().getPluginManager().registerEvents(new ChairClickListener(this), this);
        getServer().getPluginManager().registerEvents(new ChairRemovalListeners(this), this);
        super.onEnable();
        dataManager = new DataManager(this);
        chairManager = new ChairManager(this);
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

    public ChairManager getChairManager() {
        return chairManager;
    }
}

//TODO Determine if entities stay
//TODO Add Chair Right Click
//TODO Add Right Click rewards menu
//TODO Add admin right click menu
//TODO Add chair creation
//TODO Add reload
//TODO Add permissions, commands
