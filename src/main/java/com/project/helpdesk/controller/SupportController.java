package com.project.helpdesk.controller;


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.helpdesk.models.Ticket;
import com.project.helpdesk.service.SupportService;

@RestController
@RequestMapping("/ticket/support")
@CrossOrigin("*")
public class SupportController {
	
	@Autowired
	private SupportService supportService;
	
	@PostMapping("/resolve/{id}")
	public ResponseEntity<Ticket> resolveTicket(@PathVariable UUID id, @RequestParam String solution){
		return new ResponseEntity<>(supportService.resolveTicket(id, solution),HttpStatus.OK);
	}
}
