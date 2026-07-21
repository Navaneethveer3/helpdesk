package com.project.helpdesk.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;


@Service
public class AiService {

	@Autowired
	private ChatClient chatClient;

	@Autowired
	private PromptConversionService promptConversionService;
	
	@Value("classpath:/prompts/system")
	private Resource systemPrompt;

	public Flux<String> getResponseFromAssistant(String query, String userId) {
		
		String prompt = promptConversionService.convertPrompt(query);
		
		return this.chatClient
				.prompt()
				.system(system->system.text(this.systemPrompt).param("userId", userId))
				.advisors(a->a.param(ChatMemory.CONVERSATION_ID, userId))
				.user(user -> user.text(prompt))
				.stream()
				.content();
	}
}
