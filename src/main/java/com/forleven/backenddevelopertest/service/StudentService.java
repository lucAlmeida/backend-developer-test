package com.forleven.backenddevelopertest.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forleven.backenddevelopertest.dao.StudentRepository;
import com.forleven.backenddevelopertest.domain.Student;
import com.forleven.backenddevelopertest.exception.RequirementViolationException;
import com.forleven.backenddevelopertest.service.helper.StudentServiceHelper;
import com.forleven.backenddevelopertest.util.ExistsContext;
import com.forleven.backenddevelopertest.util.ServiceProperties;

@Service
public class StudentService implements IStudentService {	
	private StudentRepository studentRepository;
	
	@Autowired
	@Qualifier("studentServiceProperties")
	private ServiceProperties props;

	@Autowired
	public StudentService(StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}
	
	@Override
	@Transactional
	public List<Student> findAll() {
		return studentRepository.findAll();
	}

	@Override
	@Transactional
	public Student find(String enrollmentId) throws RequirementViolationException {
		Optional<Student> student = studentRepository.findByEnrollmentId(enrollmentId);		
		if (student.isPresent()) {
			return student.get();
		} else {
			throw new RequirementViolationException(ExistsContext.NOT_FOUND.label, props.entityName(), props.msgEnrollmentId() + " " + enrollmentId + " " + ExistsContext.NOT_FOUND.message);
		}
	}
	
	@Override
	@Transactional
	public Student save(Student student) throws RequirementViolationException {
		if (!studentRepository.existsByEnrollmentId(student.getEnrollmentId())) {
			return studentRepository.save(StudentServiceHelper.setStudentPhonesToDistinct(student));
		} else {
			throw new RequirementViolationException(ExistsContext.ALREADY_EXISTS.label, props.entityName(), props.msgEnrollmentId() + " " + student.getEnrollmentId() + " " + ExistsContext.ALREADY_EXISTS.message);
		}
	}

	@Override
	@Transactional
	public Student update(Student student) throws RequirementViolationException {
		return update(student, student.getEnrollmentId());
	}
	
	@Override
	@Transactional
	public Student update(Student student, String enrollmentId) throws RequirementViolationException {
		Optional<Student> possiblyStoredStudent = studentRepository.findByEnrollmentId(enrollmentId);
		if (possiblyStoredStudent.isPresent()) {
			Student storedStudent = possiblyStoredStudent.get();

			if (Objects.isNull(student.getEnrollmentId())) {
				student.setEnrollmentId(enrollmentId);
			}
			student.setId(storedStudent.getId());
			student.setCreatedAt(storedStudent.getCreatedAt());
			student.setPhones(StudentServiceHelper.getStudentPhonesToBeStored(student, storedStudent));

			return studentRepository.save(student);
		} else {
			throw new RequirementViolationException(ExistsContext.NOT_FOUND.label, props.entityName(), props.msgEnrollmentId() + " " + enrollmentId + " " + ExistsContext.NOT_FOUND.message);
		}
	}
	
	@Override
	@Transactional
	public boolean delete(Student student) throws RequirementViolationException {
		return delete(student.getEnrollmentId());
	}
	
	@Override
	@Transactional
	public boolean delete(String enrollmentId) throws RequirementViolationException {
		Optional<Student> student = studentRepository.findByEnrollmentId(enrollmentId);
		if (student.isPresent()) {
			return studentRepository.deleteByEnrollmentId(enrollmentId) > 0;
		} else {
			throw new RequirementViolationException(ExistsContext.NOT_FOUND.label, props.entityName(), props.msgEnrollmentId() + " " + enrollmentId + " " + ExistsContext.NOT_FOUND.message);
		}
	}
}
