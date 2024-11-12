package com.mental.health.app.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mental.health.app.service.ChatGPTService;

@RestController
@RequestMapping("/api")
public class ChatController {

    @Autowired
    private ChatGPTService chatGPTService;

    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> getChatResponse(@RequestBody Map<String, Object> request) throws IOException {
        // Extract the message history from the request body
        List<Map<String, String>> messageHistory = (List<Map<String, String>>) request.get("messages");

        // Get the response from ChatGPT
        String botResponse = chatGPTService.getChatResponse(messageHistory);

        // Return the response as JSON
        Map<String, String> response = new HashMap<>();
        response.put("response", botResponse);

        return ResponseEntity.ok(response);
    }
}
