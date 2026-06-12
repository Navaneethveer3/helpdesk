package com.project.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.helpdesk.service.AiService;

@RestController
@RequestMapping("/ai")
public class AiController {
	
	@Autowired
	private AiService service;
	
	@GetMapping("/chat")
	public ResponseEntity<String> getReponse(@RequestParam("query") String query, @RequestParam("userId") String userId){
		return new ResponseEntity<>(service.getResponseFromAssistant(query,"user"), HttpStatus.OK);
	}
}
