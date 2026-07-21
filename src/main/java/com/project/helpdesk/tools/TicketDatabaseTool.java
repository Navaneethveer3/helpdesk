package com.project.helpdesk.tools;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.helpdesk.models.Priority;
import com.project.helpdesk.models.Ticket;
import com.project.helpdesk.service.TicketService;

@Component
public class TicketDatabaseTool {

	private TicketService ticketService;

	private VectorStore vectorStore;
	
	TicketDatabaseTool(TicketService ticketService, VectorStore vectorStore) {
		this.ticketService = ticketService;
		this.vectorStore = vectorStore;
	}

	@Tool(description = "create a ticket. Only the summary will be given by the user and extract the summary clearly without any punctuation marks."
			+ " After successful creation show the complete ticket details to the user."
			+ "Before creating the ticket first search for the similar tickets from the DB."
			+ "If you find more similar tickets try to suggest the fix based on the previous solution don't create the ticket until the user forces or tells the provided solution does not work"
			+ "Ticket priority and category needs to be assigned by you based on the summary")
	public Ticket createTicket(@ToolParam(description = "Ticket summary") String summary,
			@ToolParam(description = "Ticket Priority") Priority priority,
			@ToolParam(description = "username of the user") String username, 
			@ToolParam(description="it will be given by the support agent", required=false) String solution, 
			@ToolParam(description="Ticket category") String category) {
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

	@Tool(description = "search for tickets with similar issues")
	public String getSimilarTickets(@ToolParam(description="summary of the ticket") String query) {
		List<Document> result = vectorStore.similaritySearch(
				SearchRequest.builder()
				.query(query)
				.topK(3)
				.build()
				);
		if(result==null || result.isEmpty()) {
			return "No tickets found!";
		}
		return result.stream().map(
				doc->doc.getText()
				).collect(Collectors.joining("\n\n---\n\n"));
	}
	
	@Tool(description = "get current time")
	public String getCurrentTime() {
		return String.valueOf(System.currentTimeMillis());
	}
	
	
}
