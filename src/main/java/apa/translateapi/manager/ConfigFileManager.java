package apa.translateapi.manager;


import apa.translateapi.AdvancedTranslateAPI;
import org.bukkit.configuration.file.FileConfiguration;


public class ConfigFileManager {

    private final AdvancedTranslateAPI advancedTranslateAPI;

    private static FileConfiguration configFile;

    public ConfigFileManager(AdvancedTranslateAPI advancedTranslateAPI) {
        this.advancedTranslateAPI = advancedTranslateAPI;
    }

    public void setupConfig() {
        configFile = advancedTranslateAPI.getConfig();
        advancedTranslateAPI.saveDefaultConfig();

    }


    public String getApiKey() { return configFile.getString("api-key"); }
    public String getGptAPIKEY() { return configFile.getString("gpt-api-key"); }

}