package com.project.helpdesk.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.helpdesk.tools.TicketDatabaseTool;

@Service
public class AiService {

	@Autowired
	private ChatClient chatClient;

	@Autowired
	private TicketDatabaseTool ticketDatabaseTool;

	public String getResponseFromAssistant(String query, String userId) {
		return this.chatClient
				.prompt()
				.system("You are a helpful customer support assistant. Only use tools when absolutely necessary. Once you have the information you need or have completed the action, DO NOT call the tool again. Just return your final text answer to the user.")
				.advisors(a->a.param(ChatMemory.CONVERSATION_ID, userId))
				.tools(ticketDatabaseTool)
				.user(user -> user.text(query))
				.call()
				.content();
	}
}
