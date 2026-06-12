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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public LocalDateTime getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(LocalDateTime updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	
	
}
