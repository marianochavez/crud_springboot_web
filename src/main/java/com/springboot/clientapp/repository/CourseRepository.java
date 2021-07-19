package com.springboot.clientapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.clientapp.entity.Course;

@Repository
public interface CourseRepository extends  JpaRepository<Course, Long>{
	List<Course> findByNumber(Integer number);
}
