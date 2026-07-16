package com.project.helpdesk;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.helpdesk.models.Priority;
import com.project.helpdesk.models.Ticket;
import com.project.helpdesk.service.SupportService;
import com.project.helpdesk.service.TicketService;

@SpringBootTest
class SupportServiceTests {

	@Autowired
	TicketService ticketService;
	
	@Autowired
	SupportService supportService;
	

	@Test
	void resolveTicket() {
		Ticket ticket = new Ticket();
		ticket.setUsername("user");
		ticket.setPriority(Priority.MEDIUM);
		ticket.setSummary("Login button is not working.");
		UUID id = ticketService.createTicket(ticket).getId();
		String solution = "Try again after refreshing your network";
		supportService.resolveTicket(id, solution);
		
		Ticket createdTicket = ticketService.getTicketById(id);
		assertEquals(createdTicket.getSolution(),solution);
	}
	
}
