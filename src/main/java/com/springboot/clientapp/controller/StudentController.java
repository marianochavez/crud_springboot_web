package com.springboot.clientapp.controller;

import java.util.ArrayList;
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
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.clientapp.entity.Course;
import com.springboot.clientapp.entity.Student;
import com.springboot.clientapp.repository.CourseRepository;
import com.springboot.clientapp.repository.StudentRepository;

@Controller
@RequestMapping("/students/")
public class StudentController {

	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private CourseRepository courseRepository;

	@GetMapping("list")
	public String students(Model model, @RequestParam("page") Optional<Integer> page) {

		int currentPage = page.orElse(1);

		int pageSize = 6;

		Pageable paging = PageRequest.of(currentPage - 1, pageSize);

		Page<Student> studentPage = this.studentRepository.findAll(paging);

		int totalPages = studentPage.getTotalPages();

		model.addAttribute("students", studentPage);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", totalPages);

		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		return "student/list-student";
	}

	@GetMapping("showForm")
	public String showStudentForm(Student student, Model model) {
		List<Course> course = new ArrayList<>();
		model.addAttribute("course",course);
		
		List<Course> courses = this.courseRepository.findAll();
		model.addAttribute("courses",courses);
		
		return "student/add-student";
	}

	@PostMapping("add")
	public String addStudent(@Validated Student student, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "student/add-student";
		}

		if (this.studentRepository.findByEmail(student.getEmail()).isPresent()) {
			FieldError error = new FieldError("emailerr", "email", "El email ya se encuentra en uso.");
			result.addError(error);
			return "student/add-student";
		}

		
		this.studentRepository.save(student);
		
		return "redirect:list";
	}

	@GetMapping("edit/{id}")
	public String showUpdateForm(@PathVariable("id") long id, Model model) {
		Student student = this.studentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Estudiante no válido con id : " + id));

		model.addAttribute("student", student);
		
		List<Course> course = new ArrayList<>();
		model.addAttribute("course",course);
		
		List<Course> courses = this.courseRepository.findAll();
		model.addAttribute("courses",courses);
		
		return "student/update-student";
	}

	@PostMapping("update/{id}")
	public String updateStudent(@PathVariable("id") long id, @Validated Student student, BindingResult result,
			Model model) {
		
		if (result.hasErrors()) {
			student.setId(id);
			return "student/update-student";
		}
		
		String email =  student.getEmail();
		String emailbase = this.studentRepository.findById(id).get().getEmail();
		
		if(!email.equals(emailbase)) {
			if(this.studentRepository.findByEmail(student.getEmail()).isPresent()) {
				FieldError error = new FieldError("emailerr","email","El email ya se encuentra en uso.");
				result.addError(error);
				return "student/update-student";
			}
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
