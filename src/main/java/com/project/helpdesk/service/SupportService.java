package com.project.helpdesk.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.helpdesk.models.ResolvedTickets;
import com.project.helpdesk.models.Status;
import com.project.helpdesk.models.Ticket;
import com.project.helpdesk.repository.ResolvedTicketsRepo;
import com.project.helpdesk.repository.TicketRepo;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SupportService {

	@Autowired
	private TicketRepo ticketRepo;
	
	@Autowired
	private ResolvedTicketsRepo resolvedTicketsRepo;
	
	@Autowired
	private TicketContextService ticketContextService;
	
	public Ticket resolveTicket(UUID id, String solution) {
		Ticket ticket = ticketRepo.getReferenceById(id);
		ticket.setSolution(solution);
		ticket.setStatus(Status.RESOLVED);
		ticketRepo.save(ticket);
		
		ResolvedTickets resolvedTicket = new ResolvedTickets();
		resolvedTicket.setSummary(ticket.getSummary());
		resolvedTicket.setSolution(ticket.getSolution());
		resolvedTicketsRepo.save(resolvedTicket);
		
		ticketContextService.readTicket(resolvedTicket);
		
		return ticket;
	}
	
}
