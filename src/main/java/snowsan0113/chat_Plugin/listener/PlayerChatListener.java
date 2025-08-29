package snowsan0113.chat_Plugin.listener;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import snowsan0113.chat_Plugin.Main;
import snowsan0113.chat_Plugin.manager.PlayerManager;
import snowsan0113.chat_Plugin.util.ChatUtil;
import snowsan0113.chat_Plugin.util.ConvertUtil;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class PlayerChatListener implements Listener {

    private static final Map<UUID, Integer> task_map = new HashMap<>();
    private static final FileConfiguration config;

    static {
        config = Main.getPlugin(Main.class).getConfig();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) throws ExecutionException, InterruptedException {
        Player player = event.getPlayer();
        Set<Player> recipient_set = event.getRecipients();
        String message = event.getMessage();
        String format = event.getFormat();

        recipient_set.removeIf(recipient -> {
            if (recipient.equals(player)) return false; //自分はスキップ
            PlayerManager recipientManager = new PlayerManager(recipient.getUniqueId());

            return recipientManager.getHidePlayers().contains(player.getUniqueId()) || recipientManager.getHideWord().contains(message);
        });
        event.setFormat(format + "(" + ConvertUtil.getAsyncTextToKanji(message).get() + ")");

        Bukkit.broadcastMessage(task_map.toString());
        if (PlayerManager.canChat(player)) {
            event.setCancelled(false);
            Bukkit.broadcastMessage(String.valueOf(config.getInt("send_message.timeout_time")));
            if (config.getInt("send_message.timeout_time") > 0) {
                PlayerManager.setCanChat(player, false);
                new BukkitRunnable() {
                    int time = config.getInt("send_message.timeout_time");
                    @Override
                    public void run() {
                        if (!player.isOnline()) {
                            this.cancel();
                        }
                        else {
                            if (time == 0) {
                                task_map.remove(player.getUniqueId());
                                PlayerManager.setCanChat(player, true);
                                this.cancel();
                            }
                            else {
                                PlayerManager.setCanChat(player, false);
                                time--;
                                task_map.put(player.getUniqueId(), time);
                            }
                        }
                    }
                }.runTaskTimer(Main.getPlugin(Main.class), 0L, 20L);
            }
        }
        else {
            ChatUtil.sendInfoMessage(player, String.format("送信できるまであと%d秒お待ちください。", task_map.get(player.getUniqueId())));
            event.setCancelled(true);
        }
    }

}
