package com.forleven.backenddevelopertest.service;

import java.util.List;

import com.forleven.backenddevelopertest.domain.Student;
import com.forleven.backenddevelopertest.exception.RequirementViolationException;

public interface IStudentService {
	List<Student> findAll();
	
	Student find(String enrollmentId) throws RequirementViolationException;
	
	Student save(Student student) throws RequirementViolationException;
	
	Student update(Student student) throws RequirementViolationException;
	
	Student update(Student student, String enrollmentId) throws RequirementViolationException;
	
	boolean delete(Student student) throws RequirementViolationException;
	
	boolean delete(String enrollmentId) throws RequirementViolationException;
}
