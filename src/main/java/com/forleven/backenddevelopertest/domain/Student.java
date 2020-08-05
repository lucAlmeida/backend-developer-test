package com.forleven.backenddevelopertest.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="student")
public class Student extends DomainEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="first_name")
	private String firstName;
	
	@Column(name="last_name")
	private String lastName;
	
	@Column(name="enrollment_id")
	private String enrollmentId;
	
	@OneToMany(fetch=FetchType.EAGER,
			   mappedBy="student",
			   orphanRemoval=true,
			   cascade= {CascadeType.ALL})
	private List<Phone> phones = new ArrayList<>();
	
	public Student() { }
	
	public Student(String firstName, String lastName, String enrollmentId) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.enrollmentId = enrollmentId;
	}
	
	public Student(int id, String firstName, String lastName, String enrollmentId) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.enrollmentId = enrollmentId;
	}

	public Student(int id, String firstName, String lastName, String enrollmentId, List<Phone> phones) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.enrollmentId = enrollmentId;
		this.phones = phones;
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

	public String getEnrollmentId() {
		return enrollmentId;
	}

	public void setEnrollmentId(String enrollmentId) {
		this.enrollmentId = enrollmentId;
	}

	public List<Phone> getPhones() {
		return phones;
	}

	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}
	
	public Student withPhones(List<Phone> phones) {
		setPhones(phones);
		return this;
	}
	
	public Phone addPhone(Phone phone) {
		phones.add(phone);
		return phone;
	}
	
	public int hashCode() {
		return Objects.hash(firstName, lastName, enrollmentId, phones, id);
	}
	
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Student)) return false;

		Student other = (Student) o;
		return Objects.equals(firstName, other.firstName)
				&& Objects.equals(lastName, other.firstName)
				&& Objects.equals(enrollmentId, other.enrollmentId)
				&& Objects.deepEquals(phones, other.phones)
				&& Objects.equals(id, other.id);
	}
}
