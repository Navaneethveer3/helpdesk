package com.project.helpdesk.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AiService {
	
	@Autowired
	private ChatClient chatClient;
	
	public String getResponseFromAssistant(String query) {
		return this.chatClient
				.prompt()
				.user(user->user.text(query))
				.call()
				.content();
	}
}
