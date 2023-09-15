package me.a8kj.knockffa.util;

import lombok.NonNull;
import net.md_5.bungee.api.ChatColor;

public class StringUtils {

    private StringUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String colorize(@NonNull String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}
