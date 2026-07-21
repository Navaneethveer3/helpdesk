package com.project.helpdesk.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.*;


@Service
public class WebSearchService {
	
	
	public List<String> getUrlsFromSearchEngine(String query){
		WebClient webClient = WebClient.create("https://www.searchapi.io");
		
		return webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("api/v1/search")
						.queryParam("q", query)
						.queryParam("engine", "google")
						.queryParam("api_key", "dsT2zgKv3QFVwvn6Y5AuWXLc")
						.build())
				.retrieve()
				.bodyToMono(Map.class)
				.map(response -> {
					List<String> urls = new ArrayList<>();
					if (response != null && response.containsKey("organic_results")) {
						List<Map<String, Object>> organicResults = (List<Map<String, Object>>) response.get("organic_results");
						for (Map<String, Object> result : organicResults) {
							if (result.containsKey("link")) {
								urls.add((String) result.get("link"));
							}
						}
					}
					return urls;
				})
				.block();
	}
	
	public List<String> crawlAndExtract(String startUrl, int maxSites) {
		List<String> extractedSites = new ArrayList<>();
		Set<String> visitedUrl = new HashSet<>();
		Queue<String> queue = new LinkedList<>();
		queue.add(startUrl);
		visitedUrl.add(startUrl);
		
		int processedCount = 0;
		while(!queue.isEmpty() && processedCount < maxSites) {
			String currentUrl = queue.remove();
			if(currentUrl==null) {
				continue;
			}
			try {
				processedCount++;
				Document document = Jsoup.connect(currentUrl)
						.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
						.timeout(5000)
						.get();
				document.select("script, style, nav, footer, header").remove();
				if(document.body()==null) continue;
				String cleanText = document.body().text();
				if(!cleanText.isBlank()) {
					extractedSites.add(cleanText);
				}
				Elements links = document.select("a[href]");
				for(Element link : links) {
					String absUrl = link.attr("abs:href");
					if(absUrl.startsWith(startUrl) && !visitedUrl.contains(absUrl)) {
						visitedUrl.add(absUrl);
						queue.add(absUrl);
					}
				}
			}
			catch(IOException e) {
				System.out.println("Skipping Unreachable URL: "+currentUrl+" due to "+e.getMessage());
			}
		}
		return extractedSites;
	}
}
