package com.springboot.clientapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.clientapp.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	public User findByEmail(String email);
}
	