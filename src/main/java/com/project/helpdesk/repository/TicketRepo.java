package com.project.helpdesk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.helpdesk.models.Ticket;

public interface TicketRepo extends JpaRepository<Ticket, Long> {
	
	Optional<Ticket> findByUsername(String username);
}
