package snowsan0113.chat_Plugin.command;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import snowsan0113.chat_Plugin.ChatPlugin;
import snowsan0113.chat_Plugin.manager.MuteManager;
import snowsan0113.chat_Plugin.util.PluginChatUtil;

import java.util.List;

public class MuteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender send, Command cmd, String s, String[] args) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        if (cmd.getName().equalsIgnoreCase("normal_mute")) {
            if (MuteManager.isMuted(player)) {
                PluginChatUtil.sendMessage(send, "既にミュートされています。");
            }
            else {
                PluginChatUtil.sendMessage(send, "ミュートしています..");
                MuteManager.addNormalMute(player);
                PluginChatUtil.sendMessage(send, "ミュートすることができました。");
            }
        }
        else if (cmd.getName().equalsIgnoreCase("temp_mute")) {

        }
        else if (cmd.getName().equalsIgnoreCase("get_mute")) {
            PluginChatUtil.sendMessage(send, "ミュート情報を取得しています...");
            StringBuilder builder = new StringBuilder();
            List<MuteManager.MuteData> data_list = MuteManager.getMuteData(player);
            for (MuteManager.MuteData data : data_list) {
                builder.append("---------- \n");
                builder.append("ID: " + data.id() + "\n");
                builder.append("種類：" + data.type() + "\n");
                builder.append("有効か：" + data.active() + "\n");
                builder.append("ミュート解除時間；" + data.unmuteTime() + "\n");
                builder.append("---------- \n");
            }
            PluginChatUtil.sendMessage(send, "\n" + builder.toString());
        }
        return false;
    }

}
