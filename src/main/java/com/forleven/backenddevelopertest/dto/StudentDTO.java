package com.forleven.backenddevelopertest.dto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.forleven.backenddevelopertest.domain.Phone;
import com.forleven.backenddevelopertest.domain.Student;
import com.forleven.backenddevelopertest.validation.CommonValidate;
import com.forleven.backenddevelopertest.validation.CreateValidate;
import com.forleven.backenddevelopertest.validation.DeleteValidate;
import com.forleven.backenddevelopertest.validation.UpdateValidate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonPropertyOrder({"enrollmentId", "firstName", "lastName", "phones"})
@ApiModel(value="Student")
public class StudentDTO extends DomainEntityDTO {
	@ApiModelProperty(notes="Student Enrollment ID", example="302012345", required=true, position=0)
	@NotBlank(groups={CreateValidate.class, UpdateValidate.class, DeleteValidate.class},
			  message="The enrollment ID field cannot be empty")
	@Size(groups={CommonValidate.class, DeleteValidate.class}, 
	      min=3, message="The enrollment ID field must have a size of at least 3 characters")
	@Pattern(groups={CommonValidate.class, DeleteValidate.class}, 
	         regexp="^[0-9]+$", message="The enrollment ID field must be composed of numerical digits only")
	private String enrollmentId;

	@ApiModelProperty(notes="Student First Name", example="João", required=true, position=1)
	@NotBlank(groups={CommonValidate.class}, 
			  message="The first name field cannot be empty")
	@Size(groups={CommonValidate.class}, 
		  min=3, message="The first name field must have a size of at least 3 characters")
	private String firstName;
	
	@ApiModelProperty(notes="Student Last Name", example="Silva", required=true, position=2)
	@NotBlank(groups={CommonValidate.class},
			  message="The last name field cannot be empty")
	@Size(groups={CommonValidate.class}, 
	      min=3, message="The last name field must have a size of at least 3 characters")
	private String lastName;
	
	@ApiModelProperty(notes="Student Telephones", required=false, position=3)
	@JsonProperty("phones")
	@Valid
	@NotNull(message="The phones field cannot be null")
	private List<PhoneDTO> phones = new ArrayList<>();
	
	public StudentDTO() { }

	public StudentDTO(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public StudentDTO(String firstName, String lastName, String enrollmentId) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.enrollmentId = enrollmentId;
	}

	public StudentDTO(String firstName, String lastName, String enrollmentId, List<PhoneDTO> phones) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.enrollmentId = enrollmentId;
		this.phones = phones;
	}
	
	public StudentDTO(String id, String firstName, String lastName, String enrollmentId) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.enrollmentId = enrollmentId;
	}

	public StudentDTO(String id, String firstName, String lastName, String enrollmentId, Calendar lastModified, Calendar createdAt) {
		super(id, lastModified, createdAt);
		this.firstName = firstName;
		this.lastName = lastName;
		this.enrollmentId = enrollmentId;
	}

	public StudentDTO(String id, String firstName, String lastName, String enrollmentId, List<PhoneDTO> phones, Calendar lastModified, Calendar createdAt) {
		super(id, lastModified, createdAt);
		this.firstName = firstName;
		this.lastName = lastName;
		this.enrollmentId = enrollmentId;
		this.phones = phones;
	}
	
	public String getEnrollmentId() {
		return enrollmentId;
	}

	public void setEnrollmentId(String enrollmentId) {
		this.enrollmentId = enrollmentId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public StudentDTO withEnrollmentId(String enrollmentId) {
		setEnrollmentId(enrollmentId);
		return this;
	}
	
	public List<PhoneDTO> getPhones() {
		return phones;
	}

	public void setPhones(List<PhoneDTO> phones) {
		this.phones = phones;
	}
	
	public PhoneDTO addPhone(PhoneDTO phone) {
		phones.add(phone);
		return phone;
	}
	
	public Student toStudent() {
		return new Student(StringUtils.isBlank(id) ? 0 : Integer.valueOf(id), firstName, lastName, 
				enrollmentId, phones.stream().map(PhoneDTO::toPhone).collect(Collectors.toList()));
	}
	
	public static StudentDTO from(Student student) {
		return new StudentDTO(String.valueOf(student.getId()), student.getFirstName(), student.getLastName(), String.valueOf(student.getEnrollmentId()),
				student.getPhones().stream().sorted(Comparator.comparing(Phone::getId)).map(PhoneDTO::from).collect(Collectors.toList()), student.getLastModified(), student.getCreatedAt());
	}
	
	public int hashCode() {
		return Objects.hash(firstName, lastName, enrollmentId, phones);
	}
	
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof StudentDTO)) return false;
		
		StudentDTO other = (StudentDTO) o;
		return Objects.equals(firstName, other.firstName)
				&& Objects.equals(lastName, other.firstName)
				&& Objects.equals(enrollmentId, other.enrollmentId)
				&& Objects.deepEquals(phones, other.phones);
	}
}
