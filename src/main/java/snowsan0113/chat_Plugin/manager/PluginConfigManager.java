package snowsan0113.chat_Plugin.manager;

import org.bukkit.configuration.file.FileConfiguration;
import snowsan0113.chat_Plugin.ChatPlugin;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class PluginConfigManager {

    private static final ChatPlugin plugin;
    private static final FileConfiguration config;

    static {
        plugin = ChatPlugin.getPlugin(ChatPlugin.class);
        config = plugin.getConfig();
    }

    /**
     * チャットの送信間隔を取得する関数
     * @return 現在の送信間隔をInt型で返す。（設定されていない場合は、0秒を返す。）
     */
    public static int getTimeOutTime() {
        return config.getInt(ConfigKeyType.TIME_OUT_TIME.getKey(), 0);
    }

    /**
     * チャットの送信感覚を設定する関数
     * @param time 0秒以上の数字
     */
    public static void setTimeOutTime(int time) {
        if (time <= 0) time = 0;
        config.set(ConfigKeyType.TIME_OUT_TIME.getKey(), time);
        plugin.saveConfig();
    }

    /**
     * 入力が禁止されている単語を取得する関数。
     * @return List型で返す。（設定されていない場合は、空のリストを返す）
     */
    public static List<String> getCantSendWords() {
        return (List<String>) config.getList(ConfigKeyType.CANT_SEND_WORD.getKey(), Collections.emptyList());
    }

    public enum ConfigKeyType {
        CANT_SEND_WORD("cant_send_word"),
        TIME_OUT_TIME("TIME_OUT_TIME");

        private final String key;

        ConfigKeyType(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}
