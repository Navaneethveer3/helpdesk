package com.project.helpdesk.service;

import java.util.*;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.helpdesk.models.ResolvedTickets;

@Service
public class TicketContextService {

	@Autowired
	private VectorStore vectorStore;
	
	public void readTicket(ResolvedTickets ticket) {
		String ticketContext = """
				Summary : %s
				
				Solution : %s
				""".formatted(ticket.getSummary(),ticket.getSolution());
		Document doc = new Document(
				ticketContext,
				Map.of("ticketId", ticket.getId(),
					   "summary" , ticket.getSummary(),
					   "solution", ticket.getSolution()
						)
				);
		vectorStore.add(List.of(doc));
	}
}
