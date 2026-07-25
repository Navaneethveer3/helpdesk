package com.project.helpdesk.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.memory.ChatMemory;
import java.util.UUID;

import org.springframework.ai.chat.model.ChatModel;

@Service
public class PromptConversionService {

	private final ChatClient chatClient;
	
	@Value("classpath:/prompts/promptConversionService")
	private Resource systemPrompt;

	@Autowired
	public PromptConversionService(ChatModel chatModel) {
		this.chatClient = ChatClient.builder(chatModel).build();
	}
	
	public String convertPrompt(String query) {
		String wrappedQuery = "Rewrite the following user query into a detailed prompt. DO NOT answer the query itself. Make the prompt very short but meaninigful and convert the prompt to a big one only if it seems to be needed big response. Only output the rewritten prompt.\n\nUser query: " + query + "\n\nYour output:";
		return chatClient
				.prompt()
				.options(ChatOptions.builder().model("qwen2.5-0.5b-instruct"))
				.system(this.systemPrompt)
				.user(user->user.text(wrappedQuery))
				.call()
				.content();
				
	}
	
}
