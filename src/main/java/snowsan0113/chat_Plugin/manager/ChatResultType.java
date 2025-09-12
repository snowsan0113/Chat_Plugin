package snowsan0113.chat_Plugin.manager;

public enum ChatResultType {
    SUCCESS("成功"),
    DENY_MUTE("ミュート"),
    DENY_CANT_WORD("禁止単語"),
    DENY_CONSECUTIVE_SEND("連続送信");

    private final String name;

    ChatResultType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
