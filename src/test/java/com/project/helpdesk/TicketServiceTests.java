package com.project.helpdesk;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.helpdesk.models.Priority;
import com.project.helpdesk.models.Status;
import com.project.helpdesk.models.Ticket;
import com.project.helpdesk.service.TicketService;


@SpringBootTest
class TicketServiceTests {

	@Autowired
	private TicketService ticketService;
	
	@Test
	void createTicket() {
		Ticket ticket = new Ticket();
		ticket.setSummary("Issue with user logout");
		ticket.setPriority(Priority.MEDIUM);
		ticket.setUsername("Unit Tester");
		ticket.setStatus(Status.OPEN);
		
		
		long id = this.ticketService.createTicket(ticket).getId();
		
		Ticket expectedTicket = this.ticketService.getTicketById(id);
		assertEquals(ticket.getUsername(), expectedTicket.getUsername());
		assertEquals(ticket.getPriority(), expectedTicket.getPriority());
		assertEquals(ticket.getStatus(), expectedTicket.getStatus());
	}
	
	@Test
	void getTicketByUsername() {
		Ticket ticket1 = this.ticketService.getTicketByUsername("Tester");
		Ticket ticket2 = this.ticketService.getTicketByUsername("Unit Tester");
		Ticket ticket3 = this.ticketService.getTicketByUsername("Developer");
		assertNotNull(ticket1);
		assertNotNull(ticket2);
		assertNull(ticket3);
	}
}
