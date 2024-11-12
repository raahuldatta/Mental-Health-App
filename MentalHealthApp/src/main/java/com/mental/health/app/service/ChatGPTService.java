package com.mental.health.app.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
public class ChatGPTService {

	private static final Logger logger = LoggerFactory.getLogger(ChatGPTService.class);

	@Value("${openai.api.key}")
	private String apiKey;

	@Value("${openai.api.url}")
	private String apiUrl;

	private static final Gson gson = new Gson();

	/**
	 * Sends the complete conversation history to OpenAI API for a response.
	 * 
	 * @param messageHistory The list of messages (both user and bot) in the conversation.
	 * @return The bot's response to the conversation.
	 * @throws IOException If an error occurs while sending the request.
	 */
	public String getChatResponse(List<Map<String, String>> messageHistory) throws IOException {
		logger.info("Received message history: {}", messageHistory);

		// Create HTTP client
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpPost request = new HttpPost(apiUrl);

			// Set headers
			request.addHeader("Content-Type", "application/json");
			request.addHeader("Authorization", "Bearer " + apiKey);

			// Create request body
			Map<String, Object> body = new HashMap<>();
			body.put("model", "gpt-4o"); // Use the gpt-4o model
			body.put("messages", messageHistory); // Send full message history
			body.put("max_tokens", 150);
			body.put("temperature", 0.7);

			StringEntity entity = new StringEntity(gson.toJson(body));
			request.setEntity(entity);

			logger.info("Sending request to OpenAI with payload: {}", gson.toJson(body));

			// Execute the request
			try (CloseableHttpResponse response = httpClient.execute(request)) {
				String responseBody = EntityUtils.toString(response.getEntity());
				logger.info("Received response from OpenAI: {}", responseBody);

				Map<String, Object> jsonResponse = gson.fromJson(responseBody, Map.class);

				// Parse and return the response text
				if (jsonResponse.containsKey("choices")) {
					Map<String, Object> firstChoice = ((List<Map<String, Object>>) jsonResponse.get("choices")).get(0);
					Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
					String botResponse = (String) message.get("content");
					logger.info("Parsed response: {}", botResponse);
					return botResponse;
				} else {
					logger.error("Error: Could not retrieve choices from OpenAI response: {}", responseBody);
					return "Error: Could not retrieve a response from ChatGPT.";
				}
			} catch (IOException e) {
				logger.error("Error executing request to OpenAI API: {}", e.getMessage(), e);
				throw e;
			}
		}
	}
}
