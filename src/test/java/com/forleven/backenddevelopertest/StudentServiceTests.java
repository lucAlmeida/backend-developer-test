package com.forleven.backenddevelopertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.forleven.backenddevelopertest.dao.StudentRepository;
import com.forleven.backenddevelopertest.domain.Phone;
import com.forleven.backenddevelopertest.domain.Student;
import com.forleven.backenddevelopertest.exception.RequirementViolationException;
import com.forleven.backenddevelopertest.service.StudentService;
import com.forleven.backenddevelopertest.util.ServiceProperties;

@SpringBootTest
class StudentServiceTests {
	@MockBean
	private StudentRepository repo;
	
	@MockBean
	@Qualifier("studentServiceProperties")
	private ServiceProperties props;
	
	@InjectMocks
	private StudentService service;
	
	List<Student> students;
	Student studentA;
	Student studentB;
	Student studentC;
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
		
		studentA = new Student(1, "João", "Silva", "202012345");
		
		studentB = new Student(2, "Maria", "Silva", "202012346");
		studentB.addPhone(new Phone("91234-5678", "Mobile"));
		
		studentC = new Student(3, "Joaquim", "Silva", "202012347");
		studentC.addPhone(new Phone("1234-5678", "Home"));

		students = Stream.of(studentA, studentB, studentC).collect(Collectors.toList());
	}
	
	@Test
	void findAllStudentsTest() {
		Mockito.when(repo.findAll()).thenReturn(students);
		assertEquals(3, service.findAll().size());
	}
	
	@Test
	void findStudentTest() {
		Student student = studentA;
		String enrollmentId = student.getEnrollmentId();
		
		Mockito.when(repo.findByEnrollmentId(enrollmentId)).thenReturn(Optional.of(student));
		try {
			assertEquals(service.find(enrollmentId), student);
		} catch (RequirementViolationException e) {
			fail("Student with known Enrollment ID not found!");
		}
	}

	@Test
	void findStudent_NotFoundTest() {
		Student student = studentA;
		String enrollmentId = student.getEnrollmentId();
		
		Mockito.when(repo.findByEnrollmentId(enrollmentId)).thenReturn(Optional.empty());
		assertThrows(RequirementViolationException.class, () -> service.find(enrollmentId));
	}

	@Test
	void saveStudentTest() {
		Student student = studentA;
		
		Mockito.when(repo.save(student)).thenReturn(student);
		try {
			assertEquals(service.save(student), student);
		} catch (RequirementViolationException e) {
			fail("Student with new Enrollment ID was not saved!");
		}
	}

	@Test
	void saveStudent_AlreadyExistsTest() {
		Student student = studentA;
		
		Mockito.when(repo.existsByEnrollmentId(student.getEnrollmentId())).thenReturn(true);
		assertThrows(RequirementViolationException.class, () -> service.save(student));
	}
	
	@Test
	void updateStudentTest() {
		Student student = studentA;
		Student previousStudent = SerializationUtils.clone(student);
		student.setFirstName("José");
		String enrollmentId = student.getEnrollmentId();
		
		Mockito.when(repo.findByEnrollmentId(enrollmentId)).thenReturn(Optional.of(student));
		Mockito.when(repo.save(student)).thenReturn(student);
		try {
			assertNotEquals(service.update(student).getFirstName(), previousStudent.getFirstName());
			assertEquals(service.update(student).getFirstName(), student.getFirstName());
			assertEquals(service.update(student), student);
		} catch (RequirementViolationException e) {
			fail("Student with given Enrollment ID not found!");
		}
	}

	@Test
	void updateStudent_ChangeEnrollmentIdTest() {
		Student student = studentA;
		String currEnrollmentId = student.getEnrollmentId();
		String newEnrollmentId = "202012348";
		student.setEnrollmentId(newEnrollmentId);
		
		Mockito.when(repo.findByEnrollmentId(currEnrollmentId)).thenReturn(Optional.of(student));
		Mockito.when(repo.save(student)).thenReturn(student);
		try {
			assertEquals(service.update(student, currEnrollmentId).getEnrollmentId(), newEnrollmentId);
		} catch (RequirementViolationException e) {
			fail("Student with given Enrollment ID not found!");
		}
	}

	@Test
	void updateStudent_NotFoundTest() {
		Student student = studentA;
		String enrollmentId = student.getEnrollmentId();
		Student updatedStudent = SerializationUtils.clone(student);
		updatedStudent.setFirstName("José");
		
		Mockito.when(repo.findByEnrollmentId(enrollmentId)).thenReturn(Optional.empty());
		assertThrows(RequirementViolationException.class, () -> service.update(updatedStudent));
		assertThrows(RequirementViolationException.class, () -> service.update(updatedStudent, enrollmentId));
	}
	
	@Test
	void deleteStudentTest() {
		Student student = studentA;
		String enrollmentId = student.getEnrollmentId();
		
		Mockito.when(repo.findByEnrollmentId(enrollmentId)).thenReturn(Optional.of(student));
		Mockito.when(repo.deleteByEnrollmentId(enrollmentId)).thenReturn((long)1);
		try {
			assertTrue(service.delete(enrollmentId));
			assertTrue(service.delete(student));
		} catch (RequirementViolationException e) {
			fail("Student with given Enrollment ID not found!");
		}
	}
	
	@Test
	void deleteStudent_NotFoundTest() {
		Student student = studentA;
		String enrollmentId = student.getEnrollmentId();
		
		Mockito.when(repo.findByEnrollmentId(enrollmentId)).thenReturn(Optional.empty());
		assertThrows(RequirementViolationException.class, () -> service.delete(enrollmentId));
		assertThrows(RequirementViolationException.class, () -> service.delete(student));
	}
}
