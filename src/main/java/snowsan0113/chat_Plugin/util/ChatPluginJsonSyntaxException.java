package snowsan0113.chat_Plugin.util;

/**
 * プラグイン専用の例外。
 */
public class ChatPluginJsonSyntaxException extends RuntimeException {
    public ChatPluginJsonSyntaxException(String message) {
        super(message);
    }
}
