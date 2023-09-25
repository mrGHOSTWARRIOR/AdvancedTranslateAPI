package apa.advancedtranslateapi.api.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUtils {

    public static Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    public static String translatehex (String input) {
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String color = input.substring(matcher.start(), matcher.end());
            input = input.replace(color, ChatColor.of(color) + "");
            matcher = pattern.matcher(input);
        }
        return input;
    }
    public static String translate(String input){
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', translatehex(input));
    }
    //translate all codes
    public static String translateAll(String input){

        return org.bukkit.ChatColor.translateAlternateColorCodes('&', translatehex(input));
    }


}
