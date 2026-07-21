package com.project.helpdesk;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.helpdesk.service.WebSearchService;

@SpringBootTest
class WebSearchServiceTests {

	@Autowired
	private WebSearchService service;

	@Test
	void webSearch() {
		String query = "Spring boot";
		List<String> urls = service.getUrlsFromSearchEngine(query);
		assertNotNull(urls);
		System.out.println(urls);
	}
	
	void webCrawl() {
		String url = "https://www.w3schools.com/java/java_intro.asp";
		List<String> extractedText = service.crawlAndExtract(url, 5);
		assertNotNull(extractedText);
		System.out.println(extractedText);
	}
}
