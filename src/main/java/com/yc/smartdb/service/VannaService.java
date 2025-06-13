package com.yc.smartdb.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
public class VannaService {

    private final String apiKey = "vn-ec3e67afeb094e4ca83fa3c1a9079e36";
    private final String userEmail = "patilranjit485@gmail.com";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String askVanna(String question) {
        StringBuilder responseBuilder = new StringBuilder();

        try {
            URL url = new URL("https://bigquery.vanna.ai/api/v0/chat_sse");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("VANNA-API-KEY", apiKey);
            connection.setDoOutput(true);

            Map<String, Object> payload = new HashMap<>();
            payload.put("message", question);
            payload.put("user_email", userEmail);
            payload.put("acceptable_responses", List.of("text"));

            String jsonPayload = objectMapper.writeValueAsString(payload);
            connection.getOutputStream().write(jsonPayload.getBytes());

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("data:")) {
                        String json = line.substring(5).trim();
                        JsonNode node = objectMapper.readTree(json);

                        String type = node.path("type").asText();
                        switch (type) {
                            case "text":
                                responseBuilder.append(node.path("text").asText()).append("\n");
                                break;
                            case "error":
                                responseBuilder.append("Error: ").append(node.path("error").asText()).append("\n");
                                break;
                            case "end":
                                return responseBuilder.toString().trim();
                        }

                    }
                }
            }

        } catch (Exception e) {
            return "⚠️ Exception: " + e.getMessage();
        }

        return "⚠️ No valid response received.";
    }
}
