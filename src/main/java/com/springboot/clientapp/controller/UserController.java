package com.springboot.clientapp.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.clientapp.entity.User;
import com.springboot.clientapp.repository.UserRepository;

@Controller
@RequestMapping("/admin/users/")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	
	
	@GetMapping("list")
	public String listUsers(Model model, @RequestParam("page") Optional<Integer> page) {
	    
		int currentPage = page.orElse(1);

		int pageSize = 10;

		Pageable paging = PageRequest.of(currentPage - 1, pageSize);

		Page<User> userPage = this.userRepository.findAll(paging);
		
		int totalPages = userPage.getTotalPages();

		model.addAttribute("users", userPage);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", totalPages);
		
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
	     
	    return "user/users";
	}
}
