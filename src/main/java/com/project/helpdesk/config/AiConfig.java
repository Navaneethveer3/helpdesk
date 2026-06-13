package com.project.helpdesk.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.toolsearch.ToolSearchToolCallingAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.tool.toolsearch.ToolIndex;
import org.springframework.ai.tool.toolsearch.index.vectorstore.VectorToolIndex;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.project.helpdesk.tools.KnowledgeBaseTool;
import com.project.helpdesk.tools.TicketDatabaseTool;

@Configuration
public class AiConfig {
	
	@Bean
	ToolIndex toolIndex(VectorStore vectorStore) {
		return new VectorToolIndex(vectorStore);
	}

	@Bean
	ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository) {
		return MessageWindowChatMemory.builder()
				.chatMemoryRepository(jdbcChatMemoryRepository)
				.maxMessages(3)
				.build();
	}
	
	@Bean
	public ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory, VectorStore vectorStore, ToolIndex toolIndex, TicketDatabaseTool ticketDatabaseTool, KnowledgeBaseTool knowledgeBaseTool) {
		
		Advisor smartToolAdvisor = ToolSearchToolCallingAdvisor.builder()
				.toolIndex(toolIndex)
				.maxResults(2)
				.build();
		
		return builder
				.defaultTools(ticketDatabaseTool, knowledgeBaseTool)
				.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build(), new SimpleLoggerAdvisor(), smartToolAdvisor)
				.build();
	}
}
