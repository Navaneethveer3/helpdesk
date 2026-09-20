package com.project.helpdesk.MockTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import com.project.helpdesk.controller.TicketController;
import com.project.helpdesk.service.TicketService;

@SpringBootTest
@AutoConfigureMockMvc
class TicketControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	private TicketService service;
	
	@Test
	void testGetAllTicket() throws Exception{
		mockMvc.perform(get("/ticket/all"))
			   .andExpect(status().isOk());
			   
	}

}
