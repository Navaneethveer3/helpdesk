package com.project.helpdesk.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AiService {

	@Autowired
	private ChatClient chatClient;


	public String getResponseFromAssistant(String query, String userId) {
		
		return this.chatClient
				.prompt()
				.system("You are a helpful Helpdesk Assistant. You have access to a variety of tools. " +
						"CRITICAL INSTRUCTION: To answer questions or read attached documents, you MUST first use the 'toolSearchTool' to discover the knowledge base search tool. " +
						"Once you find the knowledge base tool, DO NOT pass conversational phrases like 'tell me about' or 'what is' to it. " +
						"You MUST rewrite the user's question into dense, optimized search keywords before querying the knowledge base tool. " +
						"(Example: If the user asks 'What are the security features of Spring Boot?', you should query 'Spring Boot security features'). " +
						"If the user mentions an attached document but no specific topic, extract the topic from the document name and search for it.")
				.advisors(a->a.param(ChatMemory.CONVERSATION_ID, userId))
				.user(user -> user.text(query))
				.call()
				.content();
	}
}
