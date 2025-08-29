package snowsan0113.chat_Plugin.manager;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerManager {

    private static final JsonManager json = new JsonManager(JsonManager.FileType.PLAYER_DATA);
    private static final JsonObject raw_json = json.getRawJson();
    private static final Map<UUID, Boolean> can_chat_map = new HashMap<>();
    private final UUID uuid;

    public PlayerManager(UUID uuid) {
        this.uuid = uuid;
    }

    public Set<UUID> getHidePlayers() {
        JsonElement get_element = json.getObjectValue(uuid.toString() + ".hide_players"); //取得したJSON
        Set<UUID> hide_player_set = new HashSet<>();
        JsonArray hide_uuid_array = get_element.getAsJsonArray();
        for (JsonElement hide_uuid_element : hide_uuid_array) {
            String hide_uuid_string = hide_uuid_element.getAsString();
            UUID hide_player_uuid = UUID.fromString(hide_uuid_string);
            hide_player_set.add(hide_player_uuid);
        }
        return hide_player_set;
    }

    public Set<String> getHideWord() {
        JsonElement get_element = json.getObjectValue(uuid.toString() + ".hide_words");
        Set<String> hide_word_set = new HashSet<>();
        JsonArray hide_word_array = get_element.getAsJsonArray();
        for (JsonElement hide_word_element : hide_word_array) {
            hide_word_set.add(hide_word_element.getAsString());
        }
        return hide_word_set;
    }

    public static Map<UUID, PlayerManager> getPlayerDataMap() {
        Map<UUID, PlayerManager> map = new HashMap<>();
        Set<String> uuid_string_set = raw_json.keySet();
        List<UUID> uuid_set = uuid_string_set.stream().map(UUID::fromString).toList();
        for (UUID data_uuid : uuid_set) {
            map.put(data_uuid, new PlayerManager(data_uuid));
        }
        return map;
    }

    public static boolean canChat(Player player) {
        if (!can_chat_map.containsKey(player.getUniqueId())) {
            can_chat_map.put(player.getUniqueId(), true);
        }
        return can_chat_map.get(player.getUniqueId());
    }

    public static void setCanChat(Player player, boolean flag) {
        can_chat_map.put(player.getUniqueId(), flag);
    }

}
