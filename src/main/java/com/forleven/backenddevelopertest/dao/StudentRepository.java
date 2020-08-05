package com.forleven.backenddevelopertest.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forleven.backenddevelopertest.domain.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
	public boolean existsByEnrollmentId(String enrollmentId);
	public Optional<Student> findByEnrollmentId(String enrollmentId);
	public long deleteByEnrollmentId(String enrollmentId);
}
