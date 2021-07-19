package com.springboot.clientapp.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

import com.springboot.clientapp.entity.Course;
import com.springboot.clientapp.repository.CourseRepository;

@Controller
@RequestMapping("/courses/")
public class CourseController {

	@Autowired
	private CourseRepository courseRepository;

	@GetMapping("list")
	public String courses( @RequestParam("page") Optional<Integer> page,Model model) {

		int currentPage = page.orElse(1);

		model.addAttribute("currentPage", currentPage);

		int pageSize = 6;

		Pageable paging = PageRequest.of(currentPage - 1, pageSize);

		Page<Course> coursePage = this.courseRepository.findAll(paging);

		model.addAttribute("courses", coursePage);

		int totalPages = coursePage.getTotalPages();
		model.addAttribute("totalPages", totalPages);

		if (totalPages > 0) {

			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);

		}

		return "course/list-course";
	}

	@GetMapping("showForm")
	public String showCourseForm(Course course) {
		return "course/add-course";
	}

	@PostMapping("add")
	public String addCourse(@Validated Course course, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "course/add-course";
		}

		this.courseRepository.save(course);
		return "redirect:list";
	}

	@GetMapping("edit/{id}")
	public String showUpdateForm(@PathVariable("id") long id, Model model) {
		Course course = this.courseRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Comisión no válida con id : " + id));

		model.addAttribute("course", course);
		return "course/update-course";
	}

	@PostMapping("update/{id}")
	public String updateCourse(@PathVariable("id") long id, @Validated Course course, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			course.setId(id);
			return "course/update-course";
		}

		// update course
		courseRepository.save(course);

		// get all courses ( with update)
		model.addAttribute("courses", this.courseRepository.findAll());
		return "redirect:/courses/list";
	}

	@GetMapping("delete/{id}")
	public String deleteCourse(@PathVariable("id") long id, Model model) {

		if (courseRepository.findById(id).isPresent()) {

			if (courseRepository.findById(id).get().getStudents().size() == 0) {

				this.courseRepository.deleteById(id);

			} else {
				String message = "No se puede eliminar el curso " + id + ", contiene estudiantes inscriptos.";
				model.addAttribute("message",message);
				return "course/error-course";
			}
		} else {
			String message = "No existe un curso con el id: " + id;
			model.addAttribute("message",message);
			return "course/error-course";

		}
		
		return "redirect:/courses/list";
	}
}
