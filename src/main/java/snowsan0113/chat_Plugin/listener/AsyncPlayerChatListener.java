package snowsan0113.chat_Plugin.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import snowsan0113.chat_Plugin.api.TextConvertAPI;
import snowsan0113.chat_Plugin.manager.ChatResultType;
import snowsan0113.chat_Plugin.manager.MuteManager;
import snowsan0113.chat_Plugin.manager.PlayerManager;
import snowsan0113.chat_Plugin.manager.PluginConfigManager;
import snowsan0113.chat_Plugin.util.PluginChatUtil;

import java.util.Set;

public class AsyncPlayerChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String format = event.getFormat();
        Set<Player> recipients = event.getRecipients();
        PlayerManager manager = new PlayerManager(player);

        recipients.removeIf(recipient -> {
            PlayerManager recipient_manager = new PlayerManager(recipient);
            Bukkit.broadcastMessage(recipient_manager.getHidePlayerSet().toString());
            return recipient_manager.getHidePlayerSet().contains(player.getUniqueId());
        });

        if (getChatResultType(event) == ChatResultType.SUCCESS) {
            if (PluginConfigManager.getTimeOutTime() > 0) manager.addTimeOut();
        }
        else {
            if (getChatResultType(event) == ChatResultType.DENY_MUTE) {
                PluginChatUtil.sendMessage(player, "あなたはミュートされています。");
            }
            else if (getChatResultType(event) == ChatResultType.DENY_CONSECUTIVE_SEND) {
                PluginChatUtil.sendMessage(player, "連続送信をすることはできません。" + manager.getTimeOutTime() + "秒お待ちください。");
            }
            else if (getChatResultType(event) == ChatResultType.DENY_CANT_WORD) {
                PluginChatUtil.sendMessage(player, "禁止単語を入力することはできません。");
                Bukkit.getLogger().info("【※禁止単語のため、チャットには表示されません】<" + player.getName() + ">:" + event.getMessage());
            }
            event.setCancelled(true);
        }

        String roma_kana = TextConvertAPI.getRomaToKana(message);
        if (message.matches("[0-9+\\-*/%^(). ]+")) {
            double math_text = TextConvertAPI.getConvertMathText(message);
            event.setFormat("<" + player.getName() + ">: " + TextConvertAPI.getConvertText(roma_kana) + ChatColor.GRAY + "(" + math_text + ")");
        }
        else {
            event.setFormat("<" + player.getName() + ">: " + TextConvertAPI.getConvertText(roma_kana) + ChatColor.GRAY + "(" + message + ")");
        }
    }

    /**
     * チャットの送信結果を返す関数
     * @return {@link ChatResultType} による、結果を返す
     */
    public static ChatResultType getChatResultType(AsyncPlayerChatEvent event) {
        ChatResultType result = ChatResultType.SUCCESS;

        Player player = event.getPlayer();
        PlayerManager manager = new PlayerManager(player);
        if (MuteManager.isMuted(player)) result = ChatResultType.DENY_MUTE; //ミュートされている場合
        if (manager.getTimeOutTime() > 0) result = ChatResultType.DENY_CONSECUTIVE_SEND; //連続送信禁止によるタイムアウト中の場合
        for (String cant_word : PluginConfigManager.getCantSendWords()) {
            if (event.getMessage().contains(cant_word)) {
                result = ChatResultType.DENY_CANT_WORD; //禁止ワードが含まれている場合
                break;
            }
        }

        return result;
    }

}
