package com.project.helpdesk.MockTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class WebSearchControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void testWebSearch() throws Exception{
		String query = "What is Java? and it's applications";
		String endpoint = "/search/query";
		
		mockMvc.perform(get(endpoint)
					   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
					   .param("query", query))
			   .andExpect(status().isOk());
		
	}
	
	@Test
	public void testWebCrawler() throws Exception{
		String url = "https://w3schools.com";
		String maxSites = "3";
		String endpoint = "/search/crawl";
		
		mockMvc.perform(get(endpoint)
					   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
					   .param("url", url)
					   .param("maxSites", maxSites))
			   .andExpect(status().isOk());
		
	}

}
