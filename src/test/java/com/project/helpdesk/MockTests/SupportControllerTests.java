package com.project.helpdesk.MockTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SupportControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	
	@Test
	public void testResolveTicket() throws Exception{
		String ticketId = "db73fb8f-f821-4101-a0c1-3975fe02d34f";
		String solution = "reload the page and try again";
		
		mockMvc.perform(post("/ticket/support/resolve/"+ticketId)
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("solution", solution))
						.andExpect(jsonPath("$.category").value("Software"))
						.andExpect(jsonPath("$.solution").value("reload the page and try again"))
						.andExpect(status().isOk());
		
	}
	
	@Test
	public void testGetAllTickets() throws Exception{
		mockMvc.perform(get("/ticket/support/resolve/tickets"))
			   .andExpect(status().isOk());
		
	}
	

}
