package apa.translateapi.instance;

import org.bukkit.Bukkit;

public class PluginInstance {

    public static boolean checkPlaceholderAPI(){
        return Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
    }


}
