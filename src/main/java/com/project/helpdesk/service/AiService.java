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
				.advisors(a->a.param(ChatMemory.CONVERSATION_ID, userId))
				.user(user -> user.text(query))
				.call()
				.content();
	}
}
