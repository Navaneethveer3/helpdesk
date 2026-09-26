package com.project.helpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.helpdesk.models.Users;

public interface UsersRepository extends JpaRepository<Users, Integer> {

}
