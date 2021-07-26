package com.springboot.clientapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.springboot.clientapp.entity.User;
import com.springboot.clientapp.repository.UserRepository;

@Controller

public class IndexController {
	
	@Autowired
	private UserRepository userRepository;
	

	@GetMapping("login")
	public String login() {

		return "user/login";
	}
	
	@GetMapping("register")
	public String showRegistrationForm(Model model) {

		model.addAttribute("user", new User());

		return "user/signup_form";
	}

	@PostMapping("process_register")
	public String processRegister(User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		userRepository.save(user);

		return "user/register_success";
	}
	
	@GetMapping("logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    return "redirect:/login?logout";
	}
	
	@GetMapping("access-denied")
	public String accessDenied() {
		return "user/access_denied";
	}

	@GetMapping("/")
	public String index() {
		return "index";
	}
}
