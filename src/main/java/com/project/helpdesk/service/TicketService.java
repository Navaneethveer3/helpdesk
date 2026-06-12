package com.project.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.helpdesk.models.Ticket;
import com.project.helpdesk.repository.TicketRepo;

import jakarta.transaction.Transactional;



@Service
@Transactional
public class TicketService {
	
	@Autowired
	private TicketRepo ticketRepo;
	
	public Ticket createTicket(Ticket ticket) {
		return ticketRepo.save(ticket);
	}
	
	public Ticket getTicketById(Long ticketId) {
		return ticketRepo.findById(ticketId).orElse(null);
	}
	
	public Ticket getTicketByUsername(String username) {
		return ticketRepo.findByUsername(username).orElse(null);
	}
	
	public Ticket updateTicket(Ticket ticket, Long ticketId) throws Exception {
		var curTicket = ticketRepo.findById(ticketId);
		if(curTicket==null) {
			throw new Exception("Ticket does not exists");
		}
		else {
			return ticketRepo.save(ticket);
		}
	}
}
