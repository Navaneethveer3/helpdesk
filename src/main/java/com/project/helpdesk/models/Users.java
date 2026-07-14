package com.project.helpdesk.models;

import java.util.*;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Users {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Integer id;
	
	@Column(unique=true)
	String username;
	
	@OneToMany(cascade = CascadeType.ALL)
	List<Ticket> tickets;
}
