package com.project.helpdesk.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.helpdesk.models.Ticket;
import com.project.helpdesk.service.TicketService;

import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/ticket")
@CrossOrigin(origins = "*")
public class TicketController {
	
	@Autowired
	private TicketService ticketService;
	
	@PostMapping("/create")
	public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket){
		return new ResponseEntity<>(this.ticketService.createTicket(ticket), HttpStatus.CREATED);
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<Ticket> getTicketById(@PathVariable UUID id){
		return new ResponseEntity<>(this.ticketService.getTicketById(id), HttpStatus.OK);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<Ticket>> getAllTickets(){
		return new ResponseEntity<>(this.ticketService.getAllTickets(), HttpStatus.OK);
	}
	
	@GetMapping("/user/{username}")
	public ResponseEntity<List<Ticket>> getTicketByUsername(@PathVariable String username){
		return new ResponseEntity<>(this.ticketService.getTicketByUsername(username), HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Ticket> updateTicket(@RequestBody Ticket ticket, @PathVariable UUID id) throws Exception{
		return new ResponseEntity<>(this.ticketService.updateTicket(ticket, id), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteTicket(@PathVariable UUID id) throws Exception{
		this.ticketService.deleteTicket(id);
		return ResponseEntity.ok("Deleted Successfully");
	}
	
}
