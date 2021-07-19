package com.springboot.clientapp.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.clientapp.entity.Student;
import com.springboot.clientapp.repository.StudentRepository;

@Controller
@RequestMapping("/students/")
public class StudentController {

	@Autowired
	private StudentRepository studentRepository;

	@GetMapping("list")
	public String students(Model model, @RequestParam("page") Optional<Integer> page) {

		int currentPage = page.orElse(1);
		
		model.addAttribute("currentPage",currentPage);
		
		int pageSize = 6;

		Pageable paging = PageRequest.of(currentPage-1, pageSize);

		Page<Student> studentPage = this.studentRepository.findAll(paging);

		model.addAttribute("students", studentPage);
		
		int totalPages = studentPage.getTotalPages();
		model.addAttribute("totalPages", totalPages);

		if (totalPages > 0) {

			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);

		}

		return "student/list-student";
	}

	@GetMapping("showForm")
	public String showStudentForm(Student student) {
		return "student/add-student";
	}

	@PostMapping("add")
	public String addStudent(@Validated Student student, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "student/add-student";
		}

		this.studentRepository.save(student);
		return "redirect:list";
	}

	@GetMapping("edit/{id}")
	public String showUpdateForm(@PathVariable("id") long id, Model model) {
		Student student = this.studentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid student id : " + id));

		model.addAttribute("student", student);
		return "student/update-student";
	}

	@PostMapping("update/{id}")
	public String updateStudent(@PathVariable("id") long id, @Validated Student student, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			student.setId(id);
			return "student/update-student";
		}

		// update student
		studentRepository.save(student);

		// get all students ( with update)
		model.addAttribute("students", this.studentRepository.findAll());
		return "redirect:/students/list";
	}

	@GetMapping("delete/{id}")
	public String deleteStudent(@PathVariable("id") long id, Model model) {

		Student student = this.studentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Estudiante no válido con id : " + id));

		this.studentRepository.delete(student);
		model.addAttribute("students", this.studentRepository.findAll());
		return "redirect:/students/list";

	}
}
