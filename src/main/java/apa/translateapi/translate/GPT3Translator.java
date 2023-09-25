package apa.translateapi.translate;

import apa.translateapi.AdvancedTranslateAPI;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.*;

public class GPT3Translator {

    private final AdvancedTranslateAPI advancedTranslateAPI ;
    private final String apiKey;

    private OpenAiService service;

    public GPT3Translator(AdvancedTranslateAPI advancedTranslateAPI) {
        this.advancedTranslateAPI = advancedTranslateAPI;
        this.apiKey = advancedTranslateAPI.getConfigFileManager().getGptAPIKEY();
        service = new OpenAiService(apiKey, Duration.ZERO);

    }

    public String translateGPT3(String textToTranslate,String originalLang , String targetLang){
        String prompt =
                "Traduce al " + targetLang + " lo que esté tras dos puntos, sin traducir lo que está entre <>, comando como por ejemplo /regiter, sin traducir lo que incluya & y un carácter," +
                        " por ejemplo &7, pon todo con la traducción incluida en un bloque de código, de " + originalLang + " a " + targetLang + ".\n" +
                        "text-to-translate: " + textToTranslate + "\n" +
                        "\n" +
                        "translated-text:";

        CompletionRequest request = CompletionRequest.builder()
                .prompt(prompt)
                .model("gpt-3.5-turbo-instruct")
                .temperature(0.9D)
                //.maxTokens(countTokens(textToTranslate))
                .maxTokens(150)
                .topP(1.0D)
                .frequencyPenalty(0D)
                .presencePenalty(0.6D)
                .stop(List.of("translated-text:"))
                .build();
        return service.createCompletion(request).getChoices().get(0).getText();
    }
    public String translateGPT3Auto(String textToTranslate , String targetLang){
        String prompt =
                "Traduce al " + targetLang + " lo que esté tras text-to-translate:, sin traducir lo que está entre <>, comando como por ejemplo /regiter, sin traducir lo que incluya & y un carácter," +
                        " por ejemplo &7, pon todo con la traducción incluida en translated-text:, traducido a el idioma del texto a " + targetLang + ".\n" +
                        "text-to-translate: " + textToTranslate + "\n" +
                        "\n" +
                        "translated-text:";

        CompletionRequest request = CompletionRequest.builder()
                .prompt(prompt)
                .model("gpt-3.5-turbo-instruct")
                .temperature(0.9D)
                .maxTokens(150)
                .topP(1.0D)
                .frequencyPenalty(0D)
                .presencePenalty(0.6D)
                .stop(List.of("translated-text:"))
                .build();
        return service.createCompletion(request).getChoices().get(0).getText();
    }


    public String translate(String inputMessage, String targetLang) {
        try {
            // Define el mensaje de entrada para GPT-3.5
            String translateText =
                    "Traduce al español lo que esté tras dos puntos, " +
                            "sin traducir placeholders, " +
                            "sin traducir lo que incluya & y un carácter, por ejemplo &7, " +
                            "pon todo con la traducción incluida en un bloque de código, de inglés a español.\n" +
                            "message: " + inputMessage;

            // URL de la API de OpenAI
            String apiUrl = "https://api.openai.com/v1/engines/davinci-codex/completions";
            System.out.println("1");

            // Crear una conexión HTTP
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            System.out.println("2");
            // Configurar la conexión HTTP
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setDoOutput(true);
            System.out.println("3");

            // Crear el cuerpo de la solicitud en formato JSON
            String jsonInput = "{\"prompt\": \"" + translateText + "\", \"max_tokens\": 50}";
            byte[] postData = jsonInput.getBytes();
            System.out.println("4");

            // Enviar la solicitud al servidor
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(postData);
            }
            System.out.println("5");

            // Leer la respuesta del servidor
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            System.out.println("6");

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            System.out.println("7");

            // Cierra la conexión y otros recursos
            connection.disconnect();
            reader.close();
            System.out.println("8");
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error en la traducción";
        }
    }
}
