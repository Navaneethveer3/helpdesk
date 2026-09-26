package com.project.helpdesk.MockTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.project.helpdesk.controller.TicketController;
import com.project.helpdesk.models.Category;
import com.project.helpdesk.models.Ticket;
import com.project.helpdesk.repository.TicketRepo;
import com.project.helpdesk.service.TicketService;

@SpringBootTest
@AutoConfigureMockMvc
class TicketControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TicketRepo ticketRepo;
	
	@Test
	public void testGetAllTicket() throws Exception{
		mockMvc.perform(get("/ticket/all"))
			   .andExpect(status().isOk());
			   
	}
	
	@Test
	public void testCreateTicket() throws Exception{
		Ticket ticket = new Ticket();
		ticket.setSummary("Login issue created today");
		ticket.setUsername("tester");
		ticket.setCategory(Category.Software);
		mockMvc.perform(post("/ticket/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(
						"{\"username\":\"tester\",\"category\":\"Software\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.username").value("tester"));
		
	}
	
	@Test
	public void testGetTicketById() throws Exception{
		Ticket ticket = new Ticket();
		ticket.setSummary("Logout issue");
		ticket.setUsername("tester");
		ticket.setCategory(Category.Software);
		
		Ticket savedTicket = ticketRepo.save(ticket);
		
		mockMvc.perform(get("/ticket/id/"+savedTicket.getId()))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.username").value("tester"))
			   .andExpect(jsonPath("$.summary").value("Logout issue"));
	}
	
	@Test
	public void testGetAllTickets() throws Exception{
		mockMvc.perform(get("/ticket/all"))
			   .andExpect(status().isOk());
		
	}
	
	@Test
	public void testGetTicketByUsername() throws Exception{
		mockMvc.perform(get("/ticket/user/tester"))
			   .andExpect(status().isOk());
	}
	
	@Test
	public void testUpdateTicket() throws Exception{
		Ticket ticket = new Ticket();
		ticket.setUsername("tester");
		ticket.setSummary("ticket expired");
		ticket.setCategory(Category.Software);
		
		Ticket savedTicket = ticketRepo.save(ticket);
		
		mockMvc.perform(put("/ticket/"+savedTicket.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"summary\":\"updated ticket expired\"}"))
			   .andExpect(jsonPath("$.summary").value("updated ticket expired"))
			   .andExpect(status().isOk());
		
	}
	
	@Test
	public void testDeleteTicket() throws Exception{
		Ticket ticket = new Ticket();
		ticket.setUsername("tester");
		ticket.setSummary("ticket created");
		ticket.setCategory(Category.Software);
		
		Ticket savedTicket = ticketRepo.save(ticket);
		
		mockMvc.perform(delete("/ticket/"+savedTicket.getId()))
			   .andExpect(status().isOk());
		
	}

}
