package snowsan0113.chat_Plugin.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.apache.commons.lang3.StringUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 *
 */
public class TextConvertAPI {

    private static final Map<String, String> roma_kana = new HashMap<>(); //ローマー字 → ひらがな
    private static final Map<String, String> text_kanji_map = new HashMap<>(); //ひらがな→漢字等の保存用

    static {
        //基本
        roma_kana.put("a", "あ"); roma_kana.put("i", "い"); roma_kana.put("u", "う"); roma_kana.put("e", "え"); roma_kana.put("o", "お");
        roma_kana.put("ka", "か"); roma_kana.put("ki", "き"); roma_kana.put("ku", "く"); roma_kana.put("ke", "け"); roma_kana.put("ko", "こ");
        roma_kana.put("sa", "さ"); roma_kana.put("si", "し"); roma_kana.put("su", "す"); roma_kana.put("se", "せ"); roma_kana.put("so", "そ");
        roma_kana.put("ta", "た"); roma_kana.put("ti", "ち"); roma_kana.put("tu", "つ"); roma_kana.put("te", "て"); roma_kana.put("to", "と");
        roma_kana.put("na", "な"); roma_kana.put("ni", "に"); roma_kana.put("nu", "ぬ"); roma_kana.put("ne", "ね"); roma_kana.put("no", "の");
        roma_kana.put("ha", "は"); roma_kana.put("hi", "ひ"); roma_kana.put("hu", "ふ"); roma_kana.put("he", "へ"); roma_kana.put("ho", "ほ");
        roma_kana.put("ma", "ま"); roma_kana.put("mi", "み"); roma_kana.put("mu", "む"); roma_kana.put("me", "め"); roma_kana.put("mo", "も");
        roma_kana.put("ya", "や");                          roma_kana.put("yu", "ゆ");                        roma_kana.put("yo", "よ");
        roma_kana.put("ra", "ら"); roma_kana.put("ri", "り"); roma_kana.put("ru", "る"); roma_kana.put("re", "れ"); roma_kana.put("ro", "ろ");
        roma_kana.put("wa", "わ");                          roma_kana.put("wo", "を");                      roma_kana.put("nn", "ん");

        //その他
        roma_kana.put("tsu", "つ");
        roma_kana.put("la", "ぁ"); roma_kana.put("li", "ぃ"); roma_kana.put("lu", "ぅ"); roma_kana.put("le", "ぇ"); roma_kana.put("lo", "ぉ");
        roma_kana.put("ltu", "っ");

        //拗音
        roma_kana.put("kya", "きゃ"); roma_kana.put("kyu", "きゅ"); roma_kana.put("kyo", "きょ");
        roma_kana.put("sha", "しゃ"); roma_kana.put("shu", "しゅ"); roma_kana.put("sho", "しょ");
        roma_kana.put("sya", "しゃ"); roma_kana.put("syu", "しゅ"); roma_kana.put("syo", "しょ");
        roma_kana.put("tya", "ちゃ"); roma_kana.put("tyu", "ちゅ"); roma_kana.put("tyo", "ちょ");
        roma_kana.put("cya", "ちゃ"); roma_kana.put("cyu", "ちゅ"); roma_kana.put("cyo", "ちょ");
        roma_kana.put("nya", "にゃ"); roma_kana.put("nyu", "にゅ"); roma_kana.put("nyo", "にょ");
        roma_kana.put("hya", "ひゃ"); roma_kana.put("hyu", "ひゅ"); roma_kana.put("hyo", "ひょ");
        roma_kana.put("mya", "みゃ"); roma_kana.put("myu", "みゅ"); roma_kana.put("myo", "みょ");
        roma_kana.put("rya", "りゃ"); roma_kana.put("ryu", "りゅ"); roma_kana.put("ryo", "りょ");
        roma_kana.put("lya", "ゃ"); roma_kana.put("lyu", "ゅ"); roma_kana.put("lyo", "ょ");

        //濁音拗音
        roma_kana.put("ga", "が"); roma_kana.put("gi", "ぎ"); roma_kana.put("gu", "ぐ"); roma_kana.put("ge", "げ"); roma_kana.put("go", "ご");
        roma_kana.put("za", "ざ"); roma_kana.put("zi", "じ"); roma_kana.put("zu", "ず"); roma_kana.put("ze", "ぜ"); roma_kana.put("zo", "ぞ");
        roma_kana.put("da", "だ"); roma_kana.put("di", "ぢ"); roma_kana.put("du", "づ"); roma_kana.put("de", "で"); roma_kana.put("do", "ど");
        roma_kana.put("ba", "ば"); roma_kana.put("bi", "び"); roma_kana.put("bu", "ぶ"); roma_kana.put("be", "べ"); roma_kana.put("bo", "ぼ");
        roma_kana.put("pa", "ぱ"); roma_kana.put("pi", "ぴ"); roma_kana.put("pu", "ぷ"); roma_kana.put("pe", "ぺ"); roma_kana.put("po", "ぽ");
        roma_kana.put("gya", "ぎゃ"); roma_kana.put("gyu", "ぎゅ"); roma_kana.put("gyo", "ぎょ");
        roma_kana.put("ja", "じゃ"); roma_kana.put("ju", "じゅ"); roma_kana.put("jo", "じょ");
        roma_kana.put("bya", "びゃ"); roma_kana.put("byu", "びゅ"); roma_kana.put("byo", "びょ");
        roma_kana.put("pya", "ぴゃ"); roma_kana.put("pyu", "ぴゅ"); roma_kana.put("pyo", "ぴょ");
    }

    /**
     * ローマ字をひらがなに変換する関数
     * @param text 変換したいローマ字
     * @return 変換されたひらがな
     */
    public static String getRomaToKana(String text) {
        String[] roma_list = roma_kana.keySet().toArray(new String[0]);
        String[] kana_list = roma_kana.values().toArray(new String[0]);
        return StringUtils.replaceEach(text, roma_list, kana_list);
    }

    /**
     * テキストを「漢字・ひらがな」に変換する関数
     * @param text 変換したいテキスト
     * @return 変換された文字
     */
    public static String getConvertText(String text) {
        if (text_kanji_map.containsKey(text)) {
            return text_kanji_map.get(text);
        }

        try {
            text = text.replaceAll("　", "").replaceAll(" ", "") + ",";
            URL url = new URL("http://www.google.com/transliterate?langpair=ja-Hira|ja&text=" + URLEncoder.encode(text, StandardCharsets.UTF_8));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder content = new StringBuilder();

            String inputLine;
            while((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            con.disconnect();
            JsonArray json = (new Gson()).fromJson(content.toString(), JsonArray.class);

            String result_string = json.get(0).getAsJsonArray().get(1).getAsJsonArray().get(0).getAsString();
            text_kanji_map.put(text, result_string); //メモリ上に「テキスト->漢字」の結果を保存
            return result_string;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*

     */
    public static double getConvertMathText(String text) {
        return new ExpressionBuilder(text).build().evaluate();
    }

}
