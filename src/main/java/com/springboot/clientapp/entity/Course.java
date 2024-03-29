package com.springboot.clientapp.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Table(name = "courses")
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Min(value = 1, message = "El numero no puede ser menor a 1.")
	@Max(value = 9999, message = "El numero no puede ser mayor a 9999.")
	@Column(name = "number",unique = true, nullable = false, length = 4)
	private int number;
	
	@Column(name = "title", nullable = false, length = 200)
	private String title;

	@Column(name = "description", nullable = false, length = 500)
	private String description;
	

	@ManyToMany(targetEntity = Student.class, mappedBy = "courses", cascade = { CascadeType.PERSIST, CascadeType.DETACH,
			CascadeType.MERGE, CascadeType.REFRESH })
	private List<Student> students;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	
	
	

}
