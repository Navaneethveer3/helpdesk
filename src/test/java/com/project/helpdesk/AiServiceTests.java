package com.project.helpdesk;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.helpdesk.service.AiService;

import reactor.core.publisher.Flux;


@SpringBootTest
class AiServiceTests {
	
	@Autowired
	private AiService service;
	
	
	@Test
	void chat() {
		String query = "What is the full form of DSA in computer science?";
		Flux<String> response = this.service.getResponseFromAssistant(query, "tester");
		System.out.println(response);
	}

}
