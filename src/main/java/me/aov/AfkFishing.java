package me.aov;

import me.aov.commands.FishCommandAutoComplete;
import me.aov.commands.FishCommands;
import me.aov.listeners.BlockBreak;
import me.aov.listeners.ChairClickListener;
import me.aov.listeners.ChairRemovalListeners;
import me.aov.listeners.MenuListeners;
import me.aov.managers.ChairManager;
import me.aov.managers.DataManager;
import me.aov.managers.MenuManager;
import me.aov.objects.ChairSerialization;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;


public class AfkFishing extends JavaPlugin {

    static {
        ConfigurationSerialization.registerClass(ChairSerialization.class, "ChairSerialization");
    }

    private DataManager dataManager;
    private ChairManager chairManager;
    private MenuManager menuManager;

    @Override
    public void onEnable() {
        checkDepends();
        this.getCommand("fish").setExecutor(new FishCommands(this));
        this.getCommand("fish").setTabCompleter(new FishCommandAutoComplete(this));
        getServer().getPluginManager().registerEvents(new ChairClickListener(this), this);
        getServer().getPluginManager().registerEvents(new ChairRemovalListeners(this), this);
        getServer().getPluginManager().registerEvents(new MenuListeners(this), this);
        getServer().getPluginManager().registerEvents(new BlockBreak(this), this);
        super.onEnable();
        dataManager = new DataManager(this);
        chairManager = new ChairManager(this);
        menuManager = new MenuManager(this);
    }

    @Override
    public void onDisable() {
        dataManager.saveChairsToFile();
        chairManager.removeAllChairs();
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

    public MenuManager getMenuManager() {
        return menuManager;
    }
}

//TODO Determine if entities stay
//TODO Title Messages
//TODO Pings
//TODO Announcements
//TODO Variable Times
//TODO ActionBar
//TODO Particles, Sounds
//TODO Global Boosts