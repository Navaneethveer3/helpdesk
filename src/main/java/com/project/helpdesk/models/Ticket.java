package com.project.helpdesk.models;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="help_desk_tickets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Lob
	private String summary;
	
	@Enumerated(EnumType.STRING)
	private Priority priority;
	
	@Column(unique=true)
	private String username;
	
	private LocalDateTime createdOn;
	private LocalDateTime updatedOn;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@PrePersist
	void preSave() {
		if(this.createdOn==null) {
			this.createdOn = LocalDateTime.now();
		}
		this.updatedOn = LocalDateTime.now();
	}
	
	@PreUpdate
	void preUpdate() {
		this.updatedOn = LocalDateTime.now();
	}
	
}
