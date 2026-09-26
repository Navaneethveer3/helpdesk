package com.project.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.helpdesk.models.Users;
import com.project.helpdesk.service.UsersService;

@RestController
@RequestMapping("/user")
public class UsersContoller {

	@Autowired
	private UsersService userService;
	
	@PostMapping("/register")
	public ResponseEntity<?> register(Users user) throws Exception{
		return new ResponseEntity<>(userService.create(user), HttpStatus.CREATED);
	}
	
}
