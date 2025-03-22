package org.articlesblog.services;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public interface GigaChatService {
    String getAccessToken() throws JsonProcessingException;
    String getModel() throws JsonProcessingException;
    String getAnswer(String content, String promt) throws IOException;
}
