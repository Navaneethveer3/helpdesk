package com.project.helpdesk.models;

import java.util.UUID;

public class SupportServiceEvent {

	private Ticket ticket;
	private String userId;

	public SupportServiceEvent(Ticket ticket, String userId) {
		super();
		this.ticket = ticket;
		this.userId = userId;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	
}
