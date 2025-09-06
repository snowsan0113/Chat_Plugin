package snowsan0113.chat_Plugin.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import snowsan0113.chat_Plugin.api.TextConvertAPI;
import snowsan0113.chat_Plugin.manager.MuteManager;
import snowsan0113.chat_Plugin.util.PluginChatUtil;

public class AsyncPlayerChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String format = event.getFormat();
        if (MuteManager.isMuted(player)) {
            PluginChatUtil.sendMessage(player, "あなたはミュートされています。");
            event.setCancelled(true);
        }

        String roma_kana = TextConvertAPI.getRomaToKana(message);
        event.setFormat("<" + player.getName() + ">: " + TextConvertAPI.getConvertText(roma_kana) + ChatColor.GRAY + "(" + message + ")");
    }

}
