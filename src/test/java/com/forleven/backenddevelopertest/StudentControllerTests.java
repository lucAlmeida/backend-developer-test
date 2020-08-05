package com.forleven.backenddevelopertest;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forleven.backenddevelopertest.controller.StudentController;
import com.forleven.backenddevelopertest.domain.Phone;
import com.forleven.backenddevelopertest.domain.Student;
import com.forleven.backenddevelopertest.exception.RequirementViolationException;
import com.forleven.backenddevelopertest.service.StudentService;
import com.forleven.backenddevelopertest.util.ExistsContext;

@WebMvcTest(StudentController.class)
class StudentControllerTests {
	@MockBean
	private StudentService service;

	@Autowired
	private MockMvc mvc;

	List<Student> students;
	Student studentA;
	Student studentB;
	Student studentC;
	Student studentD;
	
	@BeforeEach
	public void init() {
		
		studentA = new Student(1, "João", "Silva", "202012345");
		
		studentB = new Student(2, "Maria", "Silva", "202012346");
		studentB.addPhone(new Phone("91234-5678", "Mobile"));
		
		studentC = new Student(3, "Joaquim", "Silva", "202012347");
		studentC.addPhone(new Phone("1234-5678", "Home"));
		studentC.addPhone(new Phone("8765-4321", "Work"));

		students = Stream.of(studentA, studentB, studentC).collect(Collectors.toList());
	}
	
	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);	
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Test
	void findAllTest() throws Exception {
		Mockito.when(service.findAll()).thenReturn(students);
		mvc.perform(get("/api/students")
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$", hasSize(3)))
					.andExpect(status().isOk());
	}
	
	@Test
	void findStudentTest() throws Exception {
		String enrollmentId = studentA.getEnrollmentId();
		
		Mockito.when(service.find(enrollmentId)).thenReturn(studentA);
		mvc.perform(get("/api/students/{enrollmentId}", enrollmentId)
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.enrollmentId", is(studentA.getEnrollmentId())))
					.andExpect(jsonPath("$.firstName", is(studentA.getFirstName())))
					.andExpect(jsonPath("$.lastName", is(studentA.getLastName())))
					.andExpect(status().isOk());
	}
	
	@Test
	void findStudent_InvalidRequestNonNumericalTest() throws Exception {
		mvc.perform(get("/api/students/{enrollmentId}", "-")
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
	}

	@Test
	void findStudent_InvalidRequestTooShortTest() throws Exception {
		mvc.perform(get("/api/students/{enrollmentId}", "12")
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
	}
	
	@Test
	void findStudent_NotFoundTest() throws Exception {
		String enrollmentId = studentA.getEnrollmentId();
		
		Mockito.when(service.find(studentA.getEnrollmentId())).thenThrow(
				new RequirementViolationException(ExistsContext.NOT_FOUND.label, "Student", "Student with Enrollment ID " + studentA.getEnrollmentId() + " " + ExistsContext.NOT_FOUND.message));
		mvc.perform(get("/api/students/{enrollmentId}", enrollmentId)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
	
	@Test
	void addStudentTest() throws Exception {
		Mockito.when(service.save(ArgumentMatchers.any(Student.class))).thenReturn(studentA);
		mvc.perform(post("/api/students")
					.content(asJsonString(studentA))
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.enrollmentId", is(studentA.getEnrollmentId())))
					.andExpect(jsonPath("$.firstName", is(studentA.getFirstName())))
					.andExpect(jsonPath("$.lastName", is(studentA.getLastName())))
					.andExpect(status().isOk());
	}

	@Test
	void addStudent_MissingEnrollmentIdTest() throws Exception {
		studentA.setEnrollmentId(null);
		
		Mockito.when(service.save(ArgumentMatchers.any(Student.class))).thenReturn(studentA);
		mvc.perform(post("/api/students")
				.content(asJsonString(studentA))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void addStudent_MissingFirstNameTest() throws Exception {
		studentA.setFirstName(null);
		
		Mockito.when(service.save(ArgumentMatchers.any(Student.class))).thenReturn(studentA);
		mvc.perform(post("/api/students")
				.content(asJsonString(studentA))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void addStudent_MissingLastNameTest() throws Exception {
		studentA.setLastName(null);
		
		Mockito.when(service.save(ArgumentMatchers.any(Student.class))).thenReturn(studentA);
		mvc.perform(post("/api/students")
				.content(asJsonString(studentA))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void addStudent_BlankEnrollmentIdTest() throws Exception {
		studentA.setEnrollmentId("");
		
		Mockito.when(service.save(ArgumentMatchers.any(Student.class))).thenReturn(studentA);
		mvc.perform(post("/api/students")
				.content(asJsonString(studentA))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void addStudent_BlankFirstNameTest() throws Exception {
		studentA.setFirstName("");
		
		Mockito.when(service.save(ArgumentMatchers.any(Student.class))).thenReturn(studentA);
		mvc.perform(post("/api/students")
				.content(asJsonString(studentA))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void addStudent_BlankLastNameTest() throws Exception {
		studentA.setLastName("");
		
		Mockito.when(service.save(ArgumentMatchers.any(Student.class))).thenReturn(studentA);
		mvc.perform(post("/api/students")
				.content(asJsonString(studentA))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void addStudent_TooShortEnrollmentIdTest() throws Exception {
		studentA.setEnrollmentId("12");
		
		Mockito.when(service.save(ArgumentMatchers.any(Student.class))).thenReturn(studentA);
		mvc.perform(post("/api/students")
				.content(asJsonString(studentA))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void addStudent_TooShortFirstNameTest() throws Exception {
		studentA.setFirstName("Zé");
		
		Mockito.when(service.save(ArgumentMatchers.any(Student.class))).thenReturn(studentA);
		mvc.perform(post("/api/students")
				.content(asJsonString(studentA))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void addStudent_TooShortLastNameTest() throws Exception {
		studentA.setLastName("De");
		
		Mockito.when(service.save(ArgumentMatchers.any(Student.class))).thenReturn(studentA);
		mvc.perform(post("/api/students")
				.content(asJsonString(studentA))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void addStudent_NonNumericalEnrollmentIdTest() throws Exception {
		studentA.setEnrollmentId("abcde");
		
		Mockito.when(service.save(ArgumentMatchers.any(Student.class))).thenReturn(studentA);
		mvc.perform(post("/api/students")
				.content(asJsonString(studentA))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void addStudent_NonInsertedPhonesFieldTest() throws Exception {
		studentA.setPhones(new ArrayList<>());
		
		Mockito.when(service.save(ArgumentMatchers.any(Student.class))).thenReturn(studentA);
		mvc.perform(post("/api/students")
				.content(asJsonString(studentA))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.enrollmentId", is(studentA.getEnrollmentId())))
				.andExpect(jsonPath("$.firstName", is(studentA.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(studentA.getLastName())))
				.andExpect(jsonPath("$.phones", hasSize(0)))
				.andExpect(status().isOk());
	}

	@Test
	void addStudent_NullPhonesFieldTest() throws Exception {
		studentA.setPhones(null);
		
		Mockito.when(service.save(ArgumentMatchers.any(Student.class))).thenReturn(studentA);
		mvc.perform(post("/api/students")
				.content(asJsonString(studentA))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void addStudent_WithPhonesTest() throws Exception {
		Student studentWithPhones = studentC;
		
		Mockito.when(service.save(ArgumentMatchers.any(Student.class))).thenReturn(studentWithPhones);
		mvc.perform(post("/api/students")
				.content(asJsonString(studentWithPhones))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.enrollmentId", is(studentWithPhones.getEnrollmentId())))
				.andExpect(jsonPath("$.firstName", is(studentWithPhones.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(studentWithPhones.getLastName())))
				.andExpect(jsonPath("$.phones", hasSize(2)))
				.andExpect(status().isOk());
	}

	@Test
	void addStudent_WithSamePhoneTest() throws Exception {
		Student student = studentA;
		student.addPhone(new Phone("91234-5678", "Home"));
		Student studentWithSamePhone = SerializationUtils.clone(student);
		student.addPhone(new Phone("91234-5678", "Home"));
		student.addPhone(new Phone("91234-5678", "Home"));
		
		Mockito.when(service.save(ArgumentMatchers.any(Student.class))).thenReturn(studentWithSamePhone);
		mvc.perform(post("/api/students")
				.content(asJsonString(student))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.enrollmentId", is(student.getEnrollmentId())))
				.andExpect(jsonPath("$.firstName", is(student.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(student.getLastName())))
				.andExpect(jsonPath("$.phones", hasSize(1)))
				.andExpect(status().isOk());
	}
	
	@Test
	void addStudent_AddingPhoneTest() throws Exception {
		Student student = studentA;
		Student studentWithPhoneAdded = SerializationUtils.clone(student);
		studentWithPhoneAdded.addPhone(new Phone("91234-5678", "Home"));
		
		Mockito.when(service.save(ArgumentMatchers.any(Student.class))).thenReturn(studentWithPhoneAdded);
		mvc.perform(post("/api/students")
				.content(asJsonString(studentWithPhoneAdded))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.enrollmentId", is(student.getEnrollmentId())))
				.andExpect(jsonPath("$.firstName", is(student.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(student.getLastName())))
				.andExpect(jsonPath("$.phones", hasSize(student.getPhones().size() + 1)))
				.andExpect(status().isOk());
	}
	
	@Test
	void addStudent_AlreadyExistsTest() throws Exception {
		Mockito.when(service.save(ArgumentMatchers.any(Student.class))).thenThrow(
				new RequirementViolationException(ExistsContext.ALREADY_EXISTS.label, "Student", "Student with Enrollment ID " + studentA.getEnrollmentId() + " " + ExistsContext.ALREADY_EXISTS.message));
		mvc.perform(post("/api/students")
				.content(asJsonString(studentA))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict());
	}
	
	@Test
	void updateStudent_Test() throws Exception {
		Student previousStudent = SerializationUtils.clone(studentA);
		Student student = studentA;
		student.setFirstName("José");
		
		Mockito.when(service.update(ArgumentMatchers.any(Student.class))).thenReturn(student);
		mvc.perform(put("/api/students")
			.content(asJsonString(student))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.enrollmentId", is(student.getEnrollmentId())))
			.andExpect(jsonPath("$.enrollmentId", is(previousStudent.getEnrollmentId())))
			.andExpect(jsonPath("$.firstName", is(student.getFirstName())))
			.andExpect(jsonPath("$.firstName", not(previousStudent.getFirstName())))
			.andExpect(jsonPath("$.lastName", is(student.getLastName())))
			.andExpect(jsonPath("$.lastName", is(previousStudent.getLastName())))
			.andExpect(status().isOk());
	}

	@Test
	void updateStudent_MissingEnrollmentIdTest() throws Exception {
		Student student = studentA;
		student.setEnrollmentId(null);
		
		Mockito.when(service.update(ArgumentMatchers.any(Student.class))).thenReturn(student);
		mvc.perform(put("/api/students")
			.content(asJsonString(student))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}


	@Test
	void updateStudent_MissingFirstNameTest() throws Exception {
		Student student = studentA;
		student.setFirstName(null);
		
		Mockito.when(service.update(ArgumentMatchers.any(Student.class))).thenReturn(student);
		mvc.perform(put("/api/students")
			.content(asJsonString(student))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}	

	@Test
	void updateStudent_MissingLastNameTest() throws Exception {
		Student student = studentA;
		student.setLastName(null);
		
		Mockito.when(service.update(ArgumentMatchers.any(Student.class))).thenReturn(student);
		mvc.perform(put("/api/students")
			.content(asJsonString(student))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	void updateStudent_BlankEnrollmentIdTest() throws Exception {
		Student student = studentA;
		student.setEnrollmentId("");
		
		Mockito.when(service.update(ArgumentMatchers.any(Student.class))).thenReturn(student);
		mvc.perform(put("/api/students")
			.content(asJsonString(student))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}


	@Test
	void updateStudent_BlankFirstNameTest() throws Exception {
		Student student = studentA;
		student.setFirstName("");
		
		Mockito.when(service.update(ArgumentMatchers.any(Student.class))).thenReturn(student);
		mvc.perform(put("/api/students")
			.content(asJsonString(student))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}	

	@Test
	void updateStudent_BlankLastNameTest() throws Exception {
		Student student = studentA;
		student.setLastName("");
		
		Mockito.when(service.update(ArgumentMatchers.any(Student.class))).thenReturn(student);
		mvc.perform(put("/api/students")
			.content(asJsonString(student))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	void updateStudent_TooShortFirstNameTest() throws Exception {
		Student student = studentA;
		student.setFirstName("Zé");
		
		Mockito.when(service.update(ArgumentMatchers.any(Student.class))).thenReturn(student);
		mvc.perform(put("/api/students")
			.content(asJsonString(student))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}	

	@Test
	void updateStudent_TooShortLastNameTest() throws Exception {
		Student student = studentA;
		student.setLastName("De");
		
		Mockito.when(service.update(ArgumentMatchers.any(Student.class))).thenReturn(student);
		mvc.perform(put("/api/students")
			.content(asJsonString(student))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	void updateStudent_TooShortEnrollmentIdTest() throws Exception {
		Student student = studentA;
		student.setEnrollmentId("12");
		
		Mockito.when(service.update(ArgumentMatchers.any(Student.class))).thenReturn(student);
		mvc.perform(put("/api/students")
			.content(asJsonString(student))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	void updateStudent_NonNumericalEnrollmentIdTest() throws Exception {
		Student student = studentA;
		student.setEnrollmentId("abcde");
		
		Mockito.when(service.update(ArgumentMatchers.any(Student.class))).thenReturn(student);
		mvc.perform(put("/api/students")
			.content(asJsonString(student))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	void updateStudent_NotFoundTest() throws Exception {
		Student student = studentA;
		student.setFirstName("José");

		Mockito.when(service.update(ArgumentMatchers.any(Student.class))).thenThrow(
				new RequirementViolationException(ExistsContext.NOT_FOUND.label, "Student", "Student with Enrollment ID " + student.getEnrollmentId() + " " + ExistsContext.NOT_FOUND.message));
		mvc.perform(put("/api/students")
				.content(asJsonString(student))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void updateStudentParamTest() throws Exception {
		Student previousStudent = SerializationUtils.clone(studentA);
		Student student = studentA;
		student.setFirstName("José");
		String enrollmentId = previousStudent.getEnrollmentId();
	
		Mockito.when(service.update(ArgumentMatchers.any(Student.class), ArgumentMatchers.any(String.class))).thenReturn(student);
		mvc.perform(put("/api/students/{enrollmentId}", enrollmentId)
				.content(asJsonString(student))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.enrollmentId", is(student.getEnrollmentId())))
				.andExpect(jsonPath("$.enrollmentId", is(previousStudent.getEnrollmentId())))
				.andExpect(jsonPath("$.firstName", is(student.getFirstName())))
				.andExpect(jsonPath("$.firstName", not(previousStudent.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(student.getLastName())))
				.andExpect(jsonPath("$.lastName", is(previousStudent.getLastName())))
				.andExpect(status().isOk());
	}

	@Test
	void updateStudentParam_ChangeEnrollmentIdTest() throws Exception {
		Student previousStudent = SerializationUtils.clone(studentA);
		Student student = studentA;
		String currEnrollmentId = previousStudent.getEnrollmentId();
		String newEnrollmentId = "202012348";
		student.setEnrollmentId(newEnrollmentId);
		
		Mockito.when(service.update(ArgumentMatchers.any(Student.class), ArgumentMatchers.any(String.class))).thenReturn(student);
		mvc.perform(put("/api/students/{enrollmentId}", currEnrollmentId)
				.content(asJsonString(student))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.enrollmentId", is(newEnrollmentId)))
				.andExpect(jsonPath("$.enrollmentId", not(currEnrollmentId)))
				.andExpect(jsonPath("$.firstName", is(student.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(student.getLastName())))
				.andExpect(status().isOk());
	}

	
	@Test
	void updateStudentParam_ChangeEnrollmentIdNotFoundTest() throws Exception {
		Student student = studentA;
		String currEnrollmentId = student.getEnrollmentId();
		String newEnrollmentId = "202012348";
		student.setEnrollmentId(newEnrollmentId);

		Mockito.when(service.update(ArgumentMatchers.any(Student.class), ArgumentMatchers.any(String.class))).thenThrow(
				new RequirementViolationException(ExistsContext.NOT_FOUND.label, "Student", "Student with Enrollment ID " + currEnrollmentId + " " + ExistsContext.NOT_FOUND.message));
		mvc.perform(put("/api/students/{enrollmentId}", currEnrollmentId)
				.content(asJsonString(student))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void updateStudentParam_MissingJsonEnrollmentIdTest() throws Exception {
		Student studentWithEnrollmentId = SerializationUtils.clone(studentA);
		String enrollmentId = studentWithEnrollmentId.getEnrollmentId();
		Student studentMissingEnrollmentId = studentA;
		studentMissingEnrollmentId.setEnrollmentId(null);
		
		Mockito.when(service.update(ArgumentMatchers.any(Student.class), ArgumentMatchers.any(String.class))).thenReturn(studentWithEnrollmentId);
		mvc.perform(put("/api/students/{enrollmentId}", enrollmentId)
				.content(asJsonString(studentMissingEnrollmentId))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.enrollmentId", is(enrollmentId)))
				.andExpect(status().isOk());
	}
	
	@Test
	void updateStudentParam_MissingFirstNameTest() throws Exception {
		Student student = studentA;
		String enrollmentId = student.getEnrollmentId();
		student.setFirstName(null);
		
		Mockito.when(service.update(ArgumentMatchers.any(Student.class), ArgumentMatchers.any(String.class))).thenReturn(student);
		mvc.perform(put("/api/students/{enrollmentId}", enrollmentId)
			.content(asJsonString(student))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}	

	@Test
	void updateStudentParam_MissingLastNameTest() throws Exception {
		Student student = studentA;
		String enrollmentId = student.getEnrollmentId();
		student.setLastName(null);
		
		Mockito.when(service.update(ArgumentMatchers.any(Student.class), ArgumentMatchers.any(String.class))).thenReturn(student);
		mvc.perform(put("/api/students/{enrollmentId}", enrollmentId)
			.content(asJsonString(student))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	void updateStudentParam_BlankJsonEnrollmentIdNotFoundTest() throws Exception {
		Student student = studentA;
		String currEnrollmentId = student.getEnrollmentId();
		String newEnrollmentId = "";
		student.setEnrollmentId(newEnrollmentId);

		Mockito.when(service.update(ArgumentMatchers.any(Student.class), ArgumentMatchers.any(String.class))).thenReturn(student);
		mvc.perform(put("/api/students/{enrollmentId}", currEnrollmentId)
				.content(asJsonString(student))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void updateStudentParam_BlankFirstNameTest() throws Exception {
		Student student = studentA;
		String enrollmentId = student.getEnrollmentId();
		student.setFirstName("");
		
		Mockito.when(service.update(ArgumentMatchers.any(Student.class), ArgumentMatchers.any(String.class))).thenReturn(student);
		mvc.perform(put("/api/students/{enrollmentId}", enrollmentId)
			.content(asJsonString(student))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}	

	@Test
	void updateStudentParam_BlankLastNameTest() throws Exception {
		Student student = studentA;
		String enrollmentId = student.getEnrollmentId();
		student.setLastName("");
		
		Mockito.when(service.update(ArgumentMatchers.any(Student.class), ArgumentMatchers.any(String.class))).thenReturn(student);
		mvc.perform(put("/api/students/{enrollmentId}", enrollmentId)
			.content(asJsonString(student))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	void updateStudentParam_TooShortJsonEnrollmentIdNotFoundTest() throws Exception {
		Student student = studentA;
		String currEnrollmentId = student.getEnrollmentId();
		String newEnrollmentId = "12";
		student.setEnrollmentId(newEnrollmentId);

		Mockito.when(service.update(ArgumentMatchers.any(Student.class), ArgumentMatchers.any(String.class))).thenReturn(student);
		mvc.perform(put("/api/students/{enrollmentId}", currEnrollmentId)
				.content(asJsonString(student))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void updateStudentParam_TooShortFirstNameTest() throws Exception {
		Student student = studentA;
		String enrollmentId = student.getEnrollmentId();
		student.setFirstName("Zé");
		
		Mockito.when(service.update(ArgumentMatchers.any(Student.class), ArgumentMatchers.any(String.class))).thenReturn(student);
		mvc.perform(put("/api/students/{enrollmentId}", enrollmentId)
			.content(asJsonString(student))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}	

	@Test
	void updateStudentParam_TooShortLastNameTest() throws Exception {
		Student student = studentA;
		String enrollmentId = student.getEnrollmentId();
		student.setLastName("De");
		
		Mockito.when(service.update(ArgumentMatchers.any(Student.class), ArgumentMatchers.any(String.class))).thenReturn(student);
		mvc.perform(put("/api/students/{enrollmentId}", enrollmentId)
			.content(asJsonString(student))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	void updateStudentParam_NonNumericalJsonEnrollmentIdNotFoundTest() throws Exception {
		Student student = studentA;
		String currEnrollmentId = student.getEnrollmentId();
		String newEnrollmentId = "abcde";
		student.setEnrollmentId(newEnrollmentId);

		Mockito.when(service.update(ArgumentMatchers.any(Student.class), ArgumentMatchers.any(String.class))).thenReturn(student);
		mvc.perform(put("/api/students/{enrollmentId}", currEnrollmentId)
				.content(asJsonString(student))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void updateStudentParam_InvalidRequestNonNumericalTest() throws Exception {
		Student student = studentA;
		student.setFirstName("José");
		mvc.perform(put("/api/students/{enrollmentId}", "-")
					.content(asJsonString(student))
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
	}

	@Test
	void updateStudentParam_InvalidRequestTooShortTest() throws Exception {
		Student student = studentA;
		student.setFirstName("José");
		mvc.perform(put("/api/students/{enrollmentId}", "12")
					.content(asJsonString(student))
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
	}

	@Test
	void updateStudentParam_NotFoundTest() throws Exception {
		Student student = studentA;
		student.setFirstName("José");
		Mockito.when(service.update(ArgumentMatchers.any(Student.class), ArgumentMatchers.any(String.class))).thenThrow(
				new RequirementViolationException(ExistsContext.NOT_FOUND.label, "Student", "Student with Enrollment ID " + student.getEnrollmentId() + " " + ExistsContext.NOT_FOUND.message));
		mvc.perform(put("/api/students/{enrollmentId}", student.getEnrollmentId())
				.content(asJsonString(student))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
	
	@Test
	void deleteStudentTest() throws Exception {
		Student student = studentA;
		String enrollmentId = student.getEnrollmentId();
		
		Mockito.when(service.delete(enrollmentId)).thenReturn(true);
		mvc.perform(delete("/api/students/{enrollmentId}", enrollmentId)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

	@Test
	void deleteStudent_InvalidRequestNonNumericalTest() throws Exception {
		Student student = studentA;
		student.setFirstName("José");
		mvc.perform(delete("/api/students/{enrollmentId}", "-")
					.content(asJsonString(student))
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
	}

	@Test
	void deleteStudent_InvalidRequestTooShortTest() throws Exception {
		Student student = studentA;
		student.setFirstName("José");
		mvc.perform(delete("/api/students/{enrollmentId}", "12")
					.content(asJsonString(student))
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
	}
	
	@Test
	void deleteStudent_NotFoundTest() throws Exception {
		Student student = studentA;
		String enrollmentId = student.getEnrollmentId();
		
		Mockito.when(service.delete(enrollmentId)).thenThrow(
				new RequirementViolationException(ExistsContext.NOT_FOUND.label, "Student", "Student with Enrollment ID " + enrollmentId + " " + ExistsContext.NOT_FOUND.message));
		mvc.perform(delete("/api/students/{enrollmentId}", enrollmentId)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteStudentNoParamTest() throws Exception {
		Student student = studentA;
		
		Mockito.when(service.delete(student)).thenReturn(true);
		mvc.perform(delete("/api/students")
				.content(asJsonString(student))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}
		
	@Test
	void deleteStudentNoParam_NotFoundTest() throws Exception {
		Student student = studentA;
		
		Mockito.when(service.delete(student)).thenThrow(
				new RequirementViolationException(ExistsContext.NOT_FOUND.label, "Student", "Student with Enrollment ID " + student.getEnrollmentId() + " " + ExistsContext.NOT_FOUND.message));
		mvc.perform(delete("/api/student")
				.content(asJsonString(student))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
}
