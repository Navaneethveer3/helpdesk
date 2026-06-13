package com.project.helpdesk.tools;

import java.util.Optional;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.helpdesk.models.Ticket;
import com.project.helpdesk.service.TicketService;

@Component
public class TicketDatabaseTool {
	
	@Autowired
	private TicketService ticketService;
	
	@Tool
	public Ticket createTicket(@ToolParam(description="Ticket details.") Ticket ticket) {
		return ticketService.createTicket(ticket);
	}
	
	@Tool
	public Ticket getTicketByUsername(String username) {
		return ticketService.getTicketByUsername(username);
	}
	
	@Tool
	public Ticket getTicketById(Long id) {
		return ticketService.getTicketById(id);
	}
	
	@Tool
	public Ticket updateTicket(Ticket ticket) throws Exception {
		long id = ticketService.getTicketByUsername(ticket.getUsername()).getId();
		return ticketService.updateTicket(ticket, id);
	}
	
	@Tool
	public void deleteTicket(Long id) throws Exception{
		ticketService.deleteTicket(id);
	}
	
	@Tool
	public String getCurrentTime() {
		return String.valueOf(System.currentTimeMillis());
	}
}
