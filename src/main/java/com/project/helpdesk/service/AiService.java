package com.project.helpdesk.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.helpdesk.tools.TicketDatabaseTool;

@Service
public class AiService {

	@Autowired
	private ChatClient chatClient;

	@Autowired
	private TicketDatabaseTool ticketDatabaseTool;
	
	@Autowired
	private VectorStore vectorStore;

	public String getResponseFromAssistant(String query, String userId) {
		
		Advisor retrievalAugmentationadvisor = RetrievalAugmentationAdvisor
							.builder()
							.documentRetriever(VectorStoreDocumentRetriever.builder()
									.similarityThreshold(0.5)
									.topK(3)
									.vectorStore(this.vectorStore)
									.build()
									)
							.build();
		
		
		
		return this.chatClient
				.prompt()
				.advisors(a->a.param(ChatMemory.CONVERSATION_ID, userId))
				.advisors(retrievalAugmentationadvisor)
//				.tools(ticketDatabaseTool)
				.user(user -> user.text(query))
				.call()
				.content();
	}
}
