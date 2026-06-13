package com.project.helpdesk;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.helpdesk.service.AiService;
import com.project.helpdesk.service.DocumentReaderService;

@SpringBootTest
class DocumentReaderServiceTests {

	@Autowired
	private DocumentReaderService docService;

	@Autowired
	private AiService aiService;
	
	@Test
	void checkLoadDocument() {
		List<Document> docs = this.docService.loadDocument();
		assertNotNull(docs);
		docs.stream().forEach(item->System.out.println(item));
	}
	
	@Test
	void checkRag() {
		String prompt = "Explain about API Security and OAuth2?";
		String response = this.aiService.getResponseFromAssistant(prompt, "tester");
		assertNotNull(response);
		System.out.println(response);
	}
}
