package snowsan0113.chat_Plugin.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ChatUtil {

    public static void sendInfoMessage(CommandSender sender, String message) {
        sendPluginMessage(sender, ChatType.INFO, message);
    }

    public static void sendErrorMessage(CommandSender sender, String message) {
        sendPluginMessage(sender, ChatType.ERROR, message);
    }

    private static void sendPluginMessage(CommandSender sender, ChatType type, String message) {
        sender.sendMessage(type.getMessage() + message);
    }

    public enum ChatType {
        INFO(ChatColor.GOLD + "[ChatPlugin] " + ChatColor.RESET),
        ERROR(ChatColor.RED + "[エラー] " + ChatColor.RESET),
        UNKNOWN(null);

        private final String message;

        ChatType(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
