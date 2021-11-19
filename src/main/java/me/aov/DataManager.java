package me.aov;

import me.aov.objects.RewardTable;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.logging.Level;

public class DataManager {

    public HashMap<String, RewardTable> rewardTableSet;

    private AfkFishing main;

    private File langFile;
    private FileConfiguration langConfiguration;

    private File menuLangFile;
    private FileConfiguration menuLangConfiguration;

    private Path dataFolderPath;
    private Path rewardsFolderPath;

    public DataManager(AfkFishing main) {
        this.main = main;
        rewardTableSet = new HashMap<>();
        generateFolder();
        generateConfigs();
        loadRewardTables();
    }

    private void loadRewardTables(){
        for(File f : rewardsFolderPath.toFile().listFiles()){
            if(f.getName().contains(".yml")){
                FileConfiguration fileConfiguration = (FileConfiguration) new YamlConfiguration();
                try{
                   fileConfiguration.load(f);
                   rewardTableSet.put(f.getName().replace(".yml", ""), new RewardTable(fileConfiguration, main));
                } catch (InvalidConfigurationException | IOException e) {
                    main.getLogger().severe("Could not load " + f.getName());
                    continue;
                }
            }else{
                main.getLogger().warning("Non-yml file \"" +f.getName() + "\" found in the reward tables");
            }
        }
    }

    private void generateFolder() {
        if (!main.getDataFolder().exists()) {
            main.getDataFolder().mkdir();
        }
        File dataFolder = new File(main.getDataFolder(), "data");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        dataFolderPath = dataFolder.toPath();
        File rewardTables = new File(main.getDataFolder(), "rewards");
        if (!rewardTables.exists()) {
            rewardTables.mkdirs();
        }
        rewardsFolderPath = rewardTables.toPath();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void generateConfigs() {
        main.getConfig().options().copyDefaults(true);
        main.saveDefaultConfig();

        this.langFile = new File(main.getDataFolder(), "lang.yml");

        if (!this.langFile.exists()) {
            this.langFile.getParentFile().mkdirs();
            main.saveResource("lang.yml", false);
        }
        this.langConfiguration = (FileConfiguration) new YamlConfiguration();
        try {
            langConfiguration.load(langFile);
        } catch (Exception e) {
            main.getServer().getLogger().log(Level.SEVERE, "Failed to load lang.yml");
        }

        this.menuLangFile = new File(main.getDataFolder(), "menu lang.yml");
        if (!this.menuLangFile.exists()) {
            this.menuLangFile.getParentFile().mkdirs();
            main.saveResource("menu lang.yml", false);
        }
        this.menuLangConfiguration = (FileConfiguration) new YamlConfiguration();
        try {
            menuLangConfiguration.load(langFile);
        } catch (Exception e) {
            main.getServer().getLogger().log(Level.SEVERE, "Failed to load menu lang.yml");
        }

        File defaultRewardsFile = new File(rewardsFolderPath.toFile(), "default rewards table.yml");
        FileConfiguration defaultRewardsFileConfiguration = (FileConfiguration) new YamlConfiguration();
        if (!defaultRewardsFile.exists()) {
            try {
                copyInputStreamToFile(main.getResource("default rewards table.yml"), defaultRewardsFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void copyInputStreamToFile(InputStream inputStream, File file)
            throws IOException {

        // append = false
        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[8192];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }

    }

    public String getLang(String s) {
        return langConfiguration.getString(s);
    }

    public FileConfiguration getLangConfiguration() {
        return langConfiguration;
    }
}
