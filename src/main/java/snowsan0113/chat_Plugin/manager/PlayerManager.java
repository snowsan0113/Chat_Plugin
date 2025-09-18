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

    /**
     * 非表示プレイヤーを取得する関数
     */
    public Set<UUID> getHidePlayerSet() {
       String uuid_string = player.getUniqueId().toString(); //プレイヤーのUUID
       JsonObject json = jsonManager.getRawElement().getAsJsonObject(); //jsonを取得する
       if (!json.has(uuid_string)) {
           json.add(uuid_string, getDefaultJson(player)); //プレイヤーのキーがない場合、追加する
       }
       JsonObject player_obj = json.getAsJsonObject(uuid_string); //プレイヤーのJson
       JsonArray hide_players = player_obj.getAsJsonArray("hide_players"); //非表示プレイヤーの配列
       Set<UUID> uuid_set = new HashSet<>();
       for (JsonElement jsonElement : hide_players) { //json上から、配列をループする。
           String get_uuid_string = jsonElement.getAsString(); //取得できた値を、文字列として取得
           UUID uuid = UUID.fromString(get_uuid_string); //取得できた値を、UUIDに変換
           uuid_set.add(uuid); //Setに追加する
       }
       return uuid_set;
    }

    public int getTimeOutTime() {
        return timeout_map.getOrDefault(player, -1);
    }

    private JsonObject getDefaultJson(OfflinePlayer player) {
        JsonObject json = new JsonObject();
        json.addProperty("name", player.getName());
        json.add("hide_players", new JsonArray());
        return json;
    }
}
