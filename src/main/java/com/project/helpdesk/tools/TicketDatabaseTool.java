package com.project.helpdesk.tools;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.helpdesk.models.Priority;
import com.project.helpdesk.models.Ticket;
import com.project.helpdesk.service.TicketService;

@Component
public class TicketDatabaseTool {

	private TicketService ticketService;

	TicketDatabaseTool(TicketService ticketService) {
		this.ticketService = ticketService;
	}

	@Tool(description = "create a ticket. Only the summary will be given by the user and remaining details need to be assigned by you based on the information given by the user.")
	public Ticket createTicket(@ToolParam(description = "Ticket summary") String summary,
			@ToolParam(description = "Ticket Priority", required = false) Priority priority,
			@ToolParam(description = "username of the user") String username) {
		Ticket ticket = new Ticket();
		ticket.setSummary(summary);
		ticket.setPriority(priority);
		ticket.setUsername(username);
		return ticketService.createTicket(ticket);
	}

	@Tool(description = "get ticket by username")
	public List<Ticket> getTicketByUsername(String username) {
		return ticketService.getTicketByUsername(username);
	}

	@Tool(description = "get ticket by id")
	public Ticket getTicketById(UUID id) {
		return ticketService.getTicketById(id);
	}

	@Tool(description = "update ticket")
	public Ticket updateTicket(@ToolParam(description="Ticket summary") String summary, UUID id) throws Exception {
		Ticket ticket = new Ticket();
		ticket.setSummary(summary);
		return ticketService.updateTicket(ticket, id);
	}

	@Tool(description = "delete ticket")
	public void deleteTicket(UUID id) throws Exception {
		ticketService.deleteTicket(id);
	}

	@Tool(description = "get current time")
	public String getCurrentTime() {
		return String.valueOf(System.currentTimeMillis());
	}
}
