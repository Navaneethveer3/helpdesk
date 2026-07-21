package com.project.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

import com.project.helpdesk.service.WebSearchService;

@RestController
@RequestMapping("/search")
public class WebSearchController {

	@Autowired
	private WebSearchService webSearchService;
	
	@GetMapping("/query")
	public ResponseEntity<List<String>> webSearch(@RequestParam String query){
		return new ResponseEntity<>(webSearchService.getUrlsFromSearchEngine(query), HttpStatus.OK);
	}
	
	@GetMapping("/crawl")
	public ResponseEntity<List<String>> webCrawler(@RequestParam String url, @RequestParam int maxSites) throws Exception{
		return new ResponseEntity<>(webSearchService.crawlAndExtract(url, maxSites), HttpStatus.OK);
	}
}
