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
	
	@Tool(description="This tool helps to create new ticket in database.")
	public Ticket createTickTool(@ToolParam(description="Ticket details.") Ticket ticket) {
		return ticketService.createTicket(ticket);
	}
	
	@Tool(description="This tool helps to get ticket by username.")
	public Ticket getTicketByUsername(@ToolParam(description="username whose ticket must be obtained") String username) {
		return ticketService.getTicketByUsername(username);
	}
	
	@Tool(description="This tools helps to get ticket by id")
	public Ticket getTicketById(@ToolParam(description="Id of ticket that must be obtained") Long id) {
		return ticketService.getTicketById(id);
	}
	
	@Tool(description="This tool helps to update the ticket")
	public Ticket updateTicket(@ToolParam(description="Information of the new ticket") Ticket ticket) throws Exception {
		long id = ticketService.getTicketByUsername(ticket.getUsername()).getId();
		return ticketService.updateTicket(ticket, id);
	}
	
	@Tool(description="This tool helps to delete the ticket")
	public void deleteTicket(@ToolParam(description="Id of the ticket that need to be deleted") Long id) throws Exception{
		ticketService.deleteTicket(id);
	}
	
	@Tool(description="This return the current system time")
	public String getCurrentTime() {
		return String.valueOf(System.currentTimeMillis());
	}
}
