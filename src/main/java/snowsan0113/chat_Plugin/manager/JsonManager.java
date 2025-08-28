package snowsan0113.chat_Plugin.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import snowsan0113.chat_Plugin.Main;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class JsonManager {

    private static final Map<FileType, JsonObject> memory_json_map = new HashMap<>();

    private final FileType type;
    private final Gson gson;

    public JsonManager(FileType type) {
        this.type = type;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        updateJson();
    }

    public JsonElement getObjectValue(String key) {
        JsonObject raw_json = getRawJson();
        String[] keys = key.split("\\."); // 「.」で区切る
        JsonObject now_json = raw_json; //jsonを代入する
        for (int n = 0; n < keys.length - 1; n++) { //keyの1個前未満をループする。（keyが2個だと、1回だけ実行）
            now_json = now_json.getAsJsonObject(keys[n]); // jsonを代入する
        }

        return now_json.get(keys[keys.length - 1]); //key数 - 1（最後のキー）を取得する
    }

    public JsonObject getRawJson() {
        return memory_json_map.get(type);
    }

    public File getFile() {
        return new File(Main.getPlugin(Main.class).getDataFolder(), type.getSavePath());
    }

    public FileType getType() {
        return type;
    }

    private boolean createJson() throws IOException {
        if (!getFile().exists()) {
            try (InputStream in = Main.class.getResourceAsStream("/" + type.getFile().getName())) {
                if (in == null) throw new FileNotFoundException(type.getFile().getName());
                Files.copy(in, type.getFile().toPath());
                updateJson();
            }
        }
        return true;
    }

    private void writeFile(String date) {
        try (BufferedWriter write = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(getFile().toPath()), StandardCharsets.UTF_8))) {
            write.write(date);
            updateJson();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateJson() {
        try {
            if (!getFile().exists()) createJson(); //無い場合は作成
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(getFile().toPath()), StandardCharsets.UTF_8))) {
                JsonObject json = gson.fromJson(reader, JsonObject.class);
                memory_json_map.put(type, json);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public enum FileType {
        PLAYER_DATA("player_data.json", "sample_player_data.json");

        private final String save_path;
        private final String sample_path;

        FileType(String path, String sample) {
            this.save_path = path;
            this.sample_path = sample;
        }

        public File getFile() {
            return new File(save_path);
        }

        public String getSavePath() {
            return save_path;
        }

        public String toString() {
            return save_path;
        }

        public String getSamplePath() {
            return sample_path;
        }
    }
}
