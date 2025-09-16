package snowsan0113.chat_Plugin.manager;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import snowsan0113.chat_Plugin.ChatPlugin;

import java.util.*;

public class PlayerManager {

    private static Map<OfflinePlayer, Integer> timeout_map = new HashMap<>();
    private static JsonManager jsonManager = new JsonManager(JsonManager.FileType.PLAYER_DATA);
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

    public Set<UUID> getHidePlayerSet() {
       JsonObject json = jsonManager.getRawElement().getAsJsonObject();
       JsonObject player_obj = json.getAsJsonObject(player.getUniqueId().toString());
       JsonArray hide_players = player_obj.getAsJsonArray("hide_players");
       Set<UUID> uuid_set = new HashSet<>();
       for (JsonElement jsonElement : hide_players) {
           String uuid_string = jsonElement.getAsString();
           UUID uuid = UUID.fromString(uuid_string);
           uuid_set.add(uuid);
       }
       return uuid_set;
    }

    public int getTimeOutTime() {
        return timeout_map.getOrDefault(player, -1);
    }
}
