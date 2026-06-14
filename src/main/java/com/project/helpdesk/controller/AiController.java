package com.project.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.helpdesk.service.AiService;

import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/ai")
@CrossOrigin(origins = "*")
public class AiController {
	
	@Autowired
	private AiService service;
	
	@GetMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public ResponseEntity<Flux<String>> getReponse(@RequestParam("query") String query, @RequestParam("userId") String userId){
		return new ResponseEntity<>(service.getResponseFromAssistant(query, userId), HttpStatus.OK);
	}
}
