package org.articlesblog.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.articlesblog.feignclient.GigaChatAuthFeignClient;
import org.articlesblog.feignclient.GigaChatGenFeignClient;
import org.articlesblog.services.GigaChatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class GigaChatServiceImpl implements GigaChatService {

    @Value("${gigachat.clientSecret}")
    private String clientSecret;

    @Value("${gigachat.clientId}")
    private String clientId;

    @Value("${gigachat.scope}")
    private String scope;

    @Value("${gigachat.model}")
    private String model;

    private final GigaChatAuthFeignClient gigaChatAuthFeignClient;
    private final GigaChatGenFeignClient gigaChatGenFeignClient;

    public String getAccessToken() throws JsonProcessingException {
        String authorization = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));
        log.info("auth data: " + authorization);
        String rqUID = UUID.randomUUID().toString();
        String body = "scope=" + scope;

        String response = gigaChatAuthFeignClient.getAccessToken("Basic " + authorization, rqUID, body);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response);
        return rootNode.get("access_token").asText();
    }

    public String getModel() throws JsonProcessingException {
        String token = getAccessToken();
        log.info("token: " + token);

        String response = gigaChatGenFeignClient.getModel("Bearer " + token);

        int startIndex = response.indexOf(model);
        if (startIndex != -1) {
            model = response.substring(startIndex, startIndex + model.length());
        }
        else {
            log.info("Модель устарела. Выберите новую");
        }

        return model;
    }

    public String getAnswer(String content, String promt) throws IOException {
        String token = getAccessToken();
        String model = getModel();
        log.info("model: " + model);

        String jsonPayload = String.format(
                new String(Files.readAllBytes(Paths.get("src/main/resources/json/gigachat_json_payload.json"))),
                model,
                promt,
                content
        );

        String response = gigaChatGenFeignClient.getAnswer("Bearer " + token, jsonPayload);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response);
        return rootNode.get("choices").get(0).get("message").get("content").asText();
    }
}