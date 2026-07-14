package com.project.helpdesk.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.helpdesk.models.Ticket;

@Repository
public interface TicketRepo extends JpaRepository<Ticket, UUID> {
	
	Optional<List<Ticket>> findByUsername(String username);
}
