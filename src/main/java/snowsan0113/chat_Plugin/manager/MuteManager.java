package snowsan0113.chat_Plugin.manager;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class MuteManager {

    private static final JsonManager json_manager = new JsonManager(JsonManager.FileType.MUTE_DATA);
    private static final JsonArray raw_json = json_manager.getRawElement().getAsJsonArray();

    /**
     * プレイヤーを期限なしミュートする関数
     * @param player ミュートしたいプレイヤー（オフラインプレイヤーでも可能）
     */
    public static void addNormalMute(OfflinePlayer player) {
        if (!isMuted(player)) {
            JsonObject mute_json_obj = getTemplateJson(
                    raw_json.size(),
                    player.getName(),
                    player.getUniqueId(),
                    LocalDateTime.now().toString(),
                    MuteType.NORMAL,
                    true
            );
            raw_json.add(mute_json_obj);
            json_manager.updateJson();
        }
    }

    /**
     * プレイヤーが現在ミュートされているかを調べる関数
     * @param player ミュートされているか確認したいプレイヤー（オフラインプレイヤーでも可能）
     * @return ミュートされている場合は true、されていない場合は falseを返す
     */
    public static boolean isMuted(OfflinePlayer player) {
        for (JsonElement element : raw_json) {
            JsonObject mute_json_obj = element.getAsJsonObject();
            String name = mute_json_obj.get("name").getAsString();
            if (player.getName().equalsIgnoreCase(name)) {
                boolean active = mute_json_obj.get("active").getAsBoolean();
                if (active) return true;
            }
        }
        return false;
    }

    /**
     * プレイヤーの、今までのミュート情報を調べる関数
     * @param player ミュート情報を調べたいプレイヤー（オフラインプレイヤーでも可能）
     * @return ミュート情報を {@link MuteData} の {@link List} として返す
     */
    public static List<MuteData> getMuteData(OfflinePlayer player) {
        List<MuteData> list = new ArrayList<>();

        if (raw_json.isEmpty()) return Collections.emptyList();
        for (JsonElement element : raw_json) {
            JsonObject mute_json_obj = element.getAsJsonObject();

            int id = mute_json_obj.get("id").getAsInt();
            String name = mute_json_obj.get("name").getAsString();
            UUID uuid = UUID.fromString(mute_json_obj.get("uuid").getAsString());
            String time = mute_json_obj.get("unmute_date").getAsString();
            MuteType type = MuteType.valueOf(mute_json_obj.get("type").getAsString());
            boolean active = mute_json_obj.get("active").getAsBoolean();

            if (player.getName().equalsIgnoreCase(name)) {
                list.add(new MuteData(id, Bukkit.getOfflinePlayer(uuid), time, type, active));
            }
        }

        return list;
    }

    /**
     * 引数から、ミュートのJSONを生成するもの
     */
    private static JsonObject getTemplateJson(int id, String name, UUID uuid, String unmute_date, MuteType type, boolean active) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", id);
        obj.addProperty("name", name);
        obj.addProperty("uuid", uuid.toString());
        obj.addProperty("unmute_date", unmute_date);
        obj.addProperty("type", type.name());
        obj.addProperty("active", active);

        return obj;
    }

    public enum MuteType {
        NORMAL,
        TEMP,
        UNKNWON;
    }

    /**
     * @param id ミュートの番号
     * @param player ミュートしたプレイヤー
     * @param unmuteTime ミュートが解除される時間
     * @param type ミュートした種類
     * @param active ミュートが現在有効か
     */
    public record MuteData(int id, OfflinePlayer player, String unmuteTime, MuteType type, boolean active) { }

}
