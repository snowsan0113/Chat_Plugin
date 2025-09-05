package snowsan0113.chat_Plugin.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import snowsan0113.chat_Plugin.ChatPlugin;

public class PluginChatUtil {

    private static final String INFO = ChatColor.GOLD + "[ChatPlugin] " + ChatColor.RESET;
    private static final String ERROR = ChatColor.RED + "[エラー] " + ChatColor.RESET;

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(INFO + message);
    }

    public static void sendGlobalMessage(String message) {
        Bukkit.broadcastMessage(INFO + message);
    }
}
