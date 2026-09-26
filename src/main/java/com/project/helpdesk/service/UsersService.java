package com.project.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.helpdesk.models.Users;
import com.project.helpdesk.repository.UsersRepository;

@Service
public class UsersService {

	@Autowired
	private UsersRepository userRepo;
	
	public Users create(Users user) throws Exception {
		try {
			Users savedUser = userRepo.save(user);
			return savedUser;
		}
		catch(Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
}
