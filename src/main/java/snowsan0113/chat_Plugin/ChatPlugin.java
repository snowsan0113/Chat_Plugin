package snowsan0113.chat_Plugin;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import snowsan0113.chat_Plugin.command.MuteCommand;
import snowsan0113.chat_Plugin.listener.AsyncPlayerChatListener;

public final class ChatPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        PluginManager plm = getServer().getPluginManager();

        //コマンド
        getCommand("normal_mute").setExecutor(new MuteCommand());
        getCommand("temp_mute").setExecutor(new MuteCommand());
        getCommand("get_mute").setExecutor(new MuteCommand());

        //イベント
        plm.registerEvents(new AsyncPlayerChatListener(), this);

        getLogger().info("プラグインが有効になりました。");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
