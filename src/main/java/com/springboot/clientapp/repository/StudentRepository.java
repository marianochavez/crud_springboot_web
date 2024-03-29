package com.springboot.clientapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.clientapp.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{
	List<Student> findByLastName(String lastName);
	Optional<Student> findByEmail(String email);
}
