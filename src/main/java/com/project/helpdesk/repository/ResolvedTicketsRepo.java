package com.project.helpdesk.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.helpdesk.models.ResolvedTickets;

@Repository
public interface ResolvedTicketsRepo extends JpaRepository<ResolvedTickets, Integer> {

}
