package apa.translateapi.translate;

import apa.translateapi.AdvancedTranslateAPI;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translate {

    private final AdvancedTranslateAPI advancedTranslateAPI;
    private final String apiKey;
    public static Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
    public static Pattern patternConvertBack = Pattern.compile("~[a-fA-F0-9]{6}~");

    public Translate(AdvancedTranslateAPI advancedTranslateAPI, String apiKey) {
        this.advancedTranslateAPI = advancedTranslateAPI;
        this.apiKey = apiKey;
    }

    public String translate(String text, String sourceLang, String targetLang) {
        try {
            String textToTranslate =  text
                    .replaceAll("&0", "~0")
                    .replaceAll("&1", "~1")
                    .replaceAll("&2", "~2")
                    .replaceAll("&3", "~3")
                    .replaceAll("&4", "~4")
                    .replaceAll("&5", "~5")
                    .replaceAll("&6", "~6")
                    .replaceAll("&7", "~7")
                    .replaceAll("&8", "~8")
                    .replaceAll("&9", "~9")
                    .replaceAll("&a", "`10")
                    .replaceAll("&b", "`11")
                    .replaceAll("&c", "`12")
                    .replaceAll("&d", "`13")
                    .replaceAll("&e", "`14")
                    .replaceAll("&f", "`15")
                    .replaceAll("&k", "`16")
                    .replaceAll("&l", "`17")
                    .replaceAll("&m", "`18")
                    .replaceAll("&n", "`19")
                    .replaceAll("&o", "`20")
                    .replaceAll("&r", "#21");
            Matcher matcher = pattern.matcher(textToTranslate);
            while (matcher.find()) {
                String color = textToTranslate.substring(matcher.start(), matcher.end());
                String replacedColor = "~" + color.substring(1) + "~"; // Elimina el símbolo "#" y agrega "~" al principio y al final
                textToTranslate = textToTranslate.replace(color, replacedColor);
                matcher = pattern.matcher(textToTranslate);
            }

            String textToTranslateReady = textToTranslate.replaceAll(" ", "+");
            String url =
                    "https://translation.googleapis.com/language/translate/v2?key=" + apiKey
                            + "&source=" + sourceLang
                            + "&target=" + targetLang
                            + "&q=" + textToTranslateReady;
            //Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&2URL: " + url));
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection)obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));

                //BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    response.append(inputLine);
                in.close();

                // Parsear la respuesta JSON
                JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                JsonObject dataObject = jsonResponse.getAsJsonObject("data");
                JsonArray translationsArray = dataObject.getAsJsonArray("translations");
                JsonObject translationObject = translationsArray.get(0).getAsJsonObject();
                String textToSend = translationObject.get("translatedText").getAsString();

                //REMOVE @@
                Pattern patternFINDER = Pattern.compile("@@(\\w+@@)");
                Matcher matcherFinder = patternFINDER.matcher(textToSend);
                textToSend = matcherFinder.replaceAll("$1");


                String modifiedTextToSend = textToSend;
                Matcher matcherNew = patternConvertBack.matcher(modifiedTextToSend);
                while (matcherNew.find()) {
                    String color = modifiedTextToSend.substring(matcherNew.start(), matcherNew.end());
                    String hexColor = "#" + color.substring(1, 7); // Elimina los "~" y agrega "#" al principio
                    modifiedTextToSend = modifiedTextToSend.replace(color, hexColor);
                    matcherNew = patternConvertBack.matcher(modifiedTextToSend);
                }
                textToSend = modifiedTextToSend;
                String readyToSend = textToSend
                        .replaceAll("~0", "&0")
                        .replaceAll("~1", "&1")
                        .replaceAll("~2", "&2")
                        .replaceAll("~3", "&3")
                        .replaceAll("~4", "&4")
                        .replaceAll("~5", "&5")
                        .replaceAll("~6", "&6")
                        .replaceAll("~7", "&7")
                        .replaceAll("~8", "&8")
                        .replaceAll("~9", "&9")
                        .replaceAll("`10", "&a")
                        .replaceAll("`11", "&b")
                        .replaceAll("`12", "&c")
                        .replaceAll("`13", "&d")
                        .replaceAll("`14", "&e")
                        .replaceAll("`15", "&f")
                        .replaceAll("`16", "&k")
                        .replaceAll("`17", "&l")
                        .replaceAll("`18", "&m")
                        .replaceAll("`19", "&n")
                        .replaceAll("`20", "&o")
                        .replaceAll("#21", "&r");

                return readyToSend;
            }
            //Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cHTTP GET request failed with response code: " + responseCode));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }



    public String getLanguage(String text){
        try {
            String textToTranslate =  text
                    .replaceAll("&0", " ")
                    .replaceAll("&1", " ")
                    .replaceAll("&2", " ")
                    .replaceAll("&3", " ")
                    .replaceAll("&4", " ")
                    .replaceAll("&5", " ")
                    .replaceAll("&6", " ")
                    .replaceAll("&7", " ")
                    .replaceAll("&8", " ")
                    .replaceAll("&9", " ")
                    .replaceAll("&a", " ")
                    .replaceAll("&b", " ")
                    .replaceAll("&c", " ")
                    .replaceAll("&d", " ")
                    .replaceAll("&e", " ")
                    .replaceAll("&f", " ")
                    .replaceAll("&k", " ")
                    .replaceAll("&l", " ")
                    .replaceAll("&m", " ")
                    .replaceAll("&n", " ")
                    .replaceAll("&o", " ")
                    .replaceAll("&r", " ");
            String lasText = URLEncoder.encode(textToTranslate, StandardCharsets.UTF_8);
            String textToTranslateReady = lasText.replaceAll(" ", "%20");
            String url =
                    "https://translation.googleapis.com/language/translate/v2/detect?key=" + apiKey
                            + "&q=" + textToTranslateReady;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection)obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    response.append(inputLine);
                in.close();

                // Parsear la respuesta JSON
                JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                JsonObject dataObject = jsonResponse.getAsJsonObject("data");
                JsonArray detectionsArray = dataObject.getAsJsonArray("detections");
                JsonArray innerArray = detectionsArray.get(0).getAsJsonArray();
                JsonObject detectionObject = innerArray.get(0).getAsJsonObject();
                return detectionObject.get("language").getAsString();
            }
            //Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cHTTP GET request failed with response code: " + responseCode));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "en";
    }



    public String stripAccents(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }

    public String removeAccents(String str) {
        String cleanText = str
                .replaceAll("Á", "A")
                .replaceAll("á", "a")
                .replaceAll("É", "E")
                .replaceAll("é", "e")
                .replaceAll("Í", "I")
                .replaceAll("í", "i")
                .replaceAll("Ó", "O")
                .replaceAll("ó", "o")
                .replaceAll("Ú", "U")
                .replaceAll("ú", "u")
                .replaceAll("Ñ", "N")
                .replaceAll("ñ", "n")
                .replaceAll("Ü", "U")
                .replaceAll("ü", "u");
        return cleanText;
    }

    public String Cleanline(String str) {

        final String ORIGINAL = "ÁáÉéÍíÓóÚúÑñÜü";
        final String REEMPLAZO = "AaEeIiOoUuNnUu";

        char[] array = str.toCharArray();
        for (int indice = 0; indice < array.length; indice++) {
            int pos = ORIGINAL.indexOf(array[indice]);
            if (pos > -1) {
                array[indice] = REEMPLAZO.charAt(pos);
            }
        }
        return new String(array);
    }

}
