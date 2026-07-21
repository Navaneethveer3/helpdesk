package com.project.helpdesk;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.helpdesk.service.PromptConversionService;

@SpringBootTest
class PromptConversionServiceTests {

	@Autowired
	private PromptConversionService service;
	
	@Test
	void convertPrompt() {
		String query = "Generative AI";
		String prompt = service.convertPrompt(query);
		System.out.println(prompt);
	}

}
