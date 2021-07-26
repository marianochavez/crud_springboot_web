package com.springboot.clientapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.clientapp.entity.User;
import com.springboot.clientapp.repository.UserRepository;

@Controller
@RequestMapping("/admin/users/")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	
	
	@GetMapping("list")
	public String listUsers(Model model) {
	    List<User> listUsers = userRepository.findAll();
	    model.addAttribute("listUsers", listUsers);
	     
	    return "user/users";
	}
}
