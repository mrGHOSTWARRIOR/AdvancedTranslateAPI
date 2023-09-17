package apa.translateapi.translate;

import apa.translateapi.AdvancedTranslateAPI;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GPT3Translator {

    private final AdvancedTranslateAPI advancedTranslateAPI ;
    private final String apiKey;

    public GPT3Translator(AdvancedTranslateAPI advancedTranslateAPI) {
        this.advancedTranslateAPI = advancedTranslateAPI;
        this.apiKey = advancedTranslateAPI.getConfigFileManager().getGptAPIKEY();
    }

    public String generateResponse(String text) {
        try {
            URL url = new URL("https://api.openai.com/v1/chat/completions");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setDoOutput(true);

            // Cuerpo de la solicitud
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-3.5-turbo");

            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", text);
            messages.add(message);

            requestBody.put("messages", messages);

            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(new ObjectMapper().writeValueAsString(requestBody));
            writer.flush();
            writer.close();

            int responseCode = conn.getResponseCode();
            System.out.println("message: "+conn.getResponseMessage());

            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String jsonResponse = response.toString();
                System.out.println(jsonResponse);
                return jsonResponse;
            } else {
                System.err.println("HTTP POST request failed with response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String chatGPT(String prompt) {
        String apiUrl = "https://api.openai.com/v1/chat/completions";
        try {
            URL obj = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");

            // Cuerpo de la solicitud
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-3.5-turbo");

            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);
            messages.add(message);

            requestBody.put("messages", messages);

            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(new ObjectMapper().writeValueAsString(requestBody));
            writer.flush();
            writer.close();

            // Respuesta de ChatGPT
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();

                // Llamada al método para extraer el mensaje.
                return extractMessageFromJSONResponse(response.toString());
            } else {
                throw new RuntimeException("Error en la solicitud HTTP: " + responseCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String extractMessageFromJSONResponse(String response) {
        int start = response.indexOf("content")+ 11;

        int end = response.indexOf("\"", start);

        return response.substring(start, end);

    }












    /*

    public String chatGPT(String userMessage) {
        try {
            String url = "https://api.openai.com/v1/engines/gpt-3.5-turbo/completions";
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + apiKey);
            con.setDoOutput(true);

            // Construye la solicitud en formato JSON
            JSONObject requestData = new JSONObject();
            JSONArray messagesArray = new JSONArray();

            // Agrega el mensaje del usuario
            JSONObject userMessageObj = new JSONObject();
            userMessageObj.put("role", "user");
            userMessageObj.put("content", userMessage);
            messagesArray.put(userMessageObj);

            requestData.put("messages", messagesArray);

            // Convierte la solicitud a una cadena JSON
            String jsonInputString = requestData.toString();

            // Envía la solicitud
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = con.getResponseCode();
            System.out.println("HTTP Response Code: " + responseCode);
            System.out.println("HTTP Response Message: " + con.getResponseMessage());

            if (responseCode == 200) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    // Parsea la respuesta JSON
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONArray choicesArray = jsonResponse.getJSONArray("choices");
                    JSONObject choice = choicesArray.getJSONObject(0); // Obtén la primera elección
                    JSONObject message = choice.getJSONObject("message");
                    return message.getString("content");
                }
            } else {
                System.err.println("HTTP POST request failed with response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

     */































    public String chatGPTtext(String text) {
        try {
            String url = "https://api.openai.com/v1/completions";
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + apiKey);

            JSONObject data = new JSONObject();
            data.put("model", "text-davinci-003");
            data.put("prompt", text);
            data.put("max_tokens", 400);
            data.put("temperature", 1.0);
            con.setDoOutput(true);

            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String jsonResponse = response.toString();
                System.out.println(jsonResponse);
                return jsonResponse;
            } else {
                System.err.println("HTTP POST request failed with response code: " + responseCode);
                // Aquí puedes lanzar una excepción personalizada o tomar otras medidas según tus necesidades
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Aquí puedes lanzar una excepción personalizada o tomar otras medidas según tus necesidades
        }

        return null;
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
