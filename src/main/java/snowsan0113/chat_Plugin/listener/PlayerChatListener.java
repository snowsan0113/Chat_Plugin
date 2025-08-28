package snowsan0113.chat_Plugin.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import snowsan0113.chat_Plugin.manager.PlayerManager;
import snowsan0113.chat_Plugin.util.ConvertUtil;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class PlayerChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) throws ExecutionException, InterruptedException {
        Player player = event.getPlayer();
        Set<Player> recipient_set = event.getRecipients();
        String message = event.getMessage();
        String format = event.getFormat();
        recipient_set.removeIf(recipient -> {
            if (recipient.equals(player)) return false; //自分はスキップ
            PlayerManager recipientManager = new PlayerManager(recipient.getUniqueId());
            return recipientManager.getHidePlayers().contains(player.getUniqueId());
        });

        event.setFormat(format + "(" + ConvertUtil.getAsyncTextToKanji(message).get() + ")");
    }

}
