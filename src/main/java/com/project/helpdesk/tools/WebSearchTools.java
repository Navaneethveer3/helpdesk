package com.project.helpdesk.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.helpdesk.service.WebSearchService;

import java.util.*;


@Component
public class WebSearchTools {
	
	@Autowired
	private WebSearchService webSearchService;
	
	@Tool(description="Get the URLs of the website from the web")
	public List<String> getUrls(String query){
		return this.webSearchService.getUrlsFromSearchEngine(query);
	}
	
	@Tool(description="Crawl through the websites and get the information")
	public List<String> webCrawl(String siteUrl, int maxSites){
		return this.webSearchService.crawlAndExtract(siteUrl, maxSites);
	}
	
	@Tool(description="Search the google and get the information. After using this tool, use the searchKnowledgeBase tool to retrieve the relevant information and answer the user request.")
	public void webSearch(String query) {
		this.webSearchService.webSearch(query);
	}
	
}
