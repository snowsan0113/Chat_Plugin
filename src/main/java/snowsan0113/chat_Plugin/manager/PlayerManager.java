package snowsan0113.chat_Plugin.manager;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import snowsan0113.chat_Plugin.ChatPlugin;

import java.util.*;

public class PlayerManager {

    private static Map<OfflinePlayer, Integer> timeout_map = new HashMap<>();
    private static int TIME_OUT_TIME = 10;

    private OfflinePlayer player;

    public PlayerManager(OfflinePlayer player) {
        this.player = player;
    }

    public void addTimeOut() {
        if (!timeout_map.containsKey(player)) {
            new BukkitRunnable() {
                int time = TIME_OUT_TIME;
                @Override
                public void run() {
                    if (time <= 0) {
                        timeout_map.remove(player);
                        this.cancel();
                    }
                    else {
                        timeout_map.put(player, time);
                        time--;
                    }
                }
            }.runTaskTimer(ChatPlugin.getPlugin(ChatPlugin.class), 0L, 20L);
        }
    }

    public int getTimeOutTime() {
        return timeout_map.get(player);
    }

    public boolean canChat() {
        if (MuteManager.isMuted(player) || timeout_map.containsKey(player)) {
            return false;
        }

        return true;
    }
}
