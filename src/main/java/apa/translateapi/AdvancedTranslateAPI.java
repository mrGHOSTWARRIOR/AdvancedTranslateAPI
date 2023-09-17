package apa.translateapi;

import apa.advancedtranslateapi.api.API;
import apa.translateapi.manager.ConfigFileManager;
import apa.translateapi.translate.GPT3Translator;
import apa.translateapi.translate.Translate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


public final class AdvancedTranslateAPI extends JavaPlugin implements Listener {

    private API api;
    private Translate translate;
    private ConfigFileManager configFileManager;
    private GPT3Translator gpt3Translator;


    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f---------------------------------"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&2&lLoading &e&lAdvancedTranslateAPI!"));

        configFileManager = new ConfigFileManager(this);
        configFileManager.setupConfig();

        api = new API(this);
        translate = new Translate(this, getConfigFileManager().getApiKey());
        gpt3Translator = new GPT3Translator(this);

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lAdvancedTranslateAPI &2&lLoaded! !"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f---------------------------------"));
    }

    @Override
    public void onDisable() {
    }

    public API getApi() { return api; }
    public Translate getTranslate()  { return translate; }
    public ConfigFileManager getConfigFileManager() { return configFileManager; }
    public GPT3Translator getGpt3Translator() { return gpt3Translator; }

}
