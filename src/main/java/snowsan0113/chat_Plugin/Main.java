package snowsan0113.chat_Plugin;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import snowsan0113.chat_Plugin.listener.PlayerChatListener;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        PluginManager plm = getServer().getPluginManager();
        plm.registerEvents(new PlayerChatListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
