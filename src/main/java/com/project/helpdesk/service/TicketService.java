package com.project.helpdesk.service;

import java.util.Optional;

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

	public Ticket getTicketById(Long ticketId) {
		return ticketRepo.findById(ticketId).orElse(null);
	}

	public Ticket getTicketByUsername(String username) {
		return ticketRepo.findByUsername(username).orElse(null);
	}

	public Ticket updateTicket(Ticket newTicket, Long ticketId) throws Exception {
		var curTicket = ticketRepo.findById(ticketId);
		if (curTicket.isEmpty()) {
			throw new Exception("Ticket does not exists");
		}
		Ticket ticket = curTicket.get();
		if(newTicket.getUpdatedOn()!=null) {
			ticket.setUpdatedOn(newTicket.getUpdatedOn());
		}
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

	public void deleteTicket(Long id) throws Exception {
		Optional<Ticket> ticket = ticketRepo.findById(id);
		if (ticket == null) {
			throw new Exception("Ticket doesn't exists");
		}
		ticketRepo.deleteById(id);
	}
}
