package com.forleven.backenddevelopertest.domain;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="phone")
public class Phone extends DomainEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="phone_number")
	private String phoneNumber;
	
	@Column(name="phone_type")
	private String phoneType;

	@ManyToOne(cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name="student_id")
	private Student student;
	
	public Phone() { }

	public Phone(String phoneNumber, String phoneType) {
		super();
		this.phoneNumber = phoneNumber;
		this.phoneType = phoneType;
	}

	public Phone(int id, String phoneNumber, String phoneType, Calendar lastModified, Calendar createdAt) {
		super(id, lastModified, createdAt);
		this.phoneNumber = phoneNumber;
		this.phoneType = phoneType;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
	
	public Phone withStudent(Student student) {
		setStudent(student);
		return this;
	}
	
	public int hashCode() {
		return Objects.hash(phoneNumber, phoneType, student.id);
	}
	
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Phone)) return false;
		
		Phone other = (Phone) o;
		return Objects.equals(phoneNumber, other.phoneNumber)
				&& Objects.equals(phoneType, other.phoneType)
				&& Objects.equals(student.id, other.student.id);
	}
}
