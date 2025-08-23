package snowsan0113.chat_Plugin.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;
import snowsan0113.chat_Plugin.util.ConvertUtil;

import java.util.concurrent.ExecutionException;

public class PlayerChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) throws ExecutionException, InterruptedException {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String format = event.getFormat();
        event.setFormat(format + "(" + ConvertUtil.getAsyncTextToKanji(message).get() + ")");
    }

}
