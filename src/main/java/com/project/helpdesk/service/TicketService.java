package com.project.helpdesk.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.helpdesk.models.Status;
import com.project.helpdesk.models.Ticket;
import com.project.helpdesk.repository.TicketRepo;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TicketService {

	@Autowired
	private TicketRepo ticketRepo;

	public Ticket createTicket(Ticket ticket) {
		ticket.setStatus(Status.OPEN);
		return ticketRepo.save(ticket);
	}

	public Ticket getTicketById(UUID ticketId) {
		return ticketRepo.findById(ticketId).orElse(null);
	}
	
	public List<Ticket> getAllTickets() {
		return ticketRepo.findAll();
	}

	public List<Ticket> getTicketByUsername(String username) {
		return ticketRepo.findByUsername(username).orElse(null);
	}

	public Ticket updateTicket(Ticket newTicket, UUID ticketId) throws Exception {
		var curTicket = ticketRepo.findById(ticketId);
		if (curTicket.isEmpty()) {
			throw new Exception("Ticket does not exists");
		}
		Ticket ticket = curTicket.get();
		ticket.setUpdatedOn(LocalDateTime.now());
		if(newTicket.getCreatedOn()!=null) {
			ticket.setCreatedOn(newTicket.getCreatedOn());
		}
		if(newTicket.getPriority()!=null) {
			ticket.setPriority(newTicket.getPriority());
		}
		if(newTicket.getStatus()!=null) {
			ticket.setStatus(newTicket.getStatus());
		}
		if(newTicket.getSummary()!=null) {
			ticket.setSummary(newTicket.getSummary());
		}
		return ticketRepo.save(ticket);
	}

	public void deleteTicket(UUID id) throws Exception {
		Optional<Ticket> ticket = ticketRepo.findById(id);
		if (ticket.isEmpty()) {
			throw new Exception("Ticket doesn't exist");
		}
		ticketRepo.deleteById(id);
	}
}
