package com.forleven.backenddevelopertest.dto;

import java.util.Calendar;
import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.forleven.backenddevelopertest.domain.Phone;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonPropertyOrder({"phoneNumber", "phoneType"})
@ApiModel(value="Phone")
public class PhoneDTO extends DomainEntityDTO {
	@ApiModelProperty(notes="Student Telephone Number", example="91234-5678", required=true, position=0)
	@NotBlank(message="The phone number field cannot be empty")
	@Size(min=3, max=20, message="The phone number field must have a size of at least 3 and at most 20 characters")
	private String phoneNumber;

	@ApiModelProperty(notes="Student Telephone Type", example="Mobile", required=true, position=1)
	@NotBlank(message="The phone type field cannot be empty")	
	private String phoneType;
	
	public PhoneDTO() { }

	public PhoneDTO(String phoneNumber, String phoneType) {
		this.phoneNumber = phoneNumber;
		this.phoneType = phoneType;
	}

	public PhoneDTO(String id, String phoneNumber, String phoneType) {
		this.id = id;
		this.phoneNumber = phoneNumber;
		this.phoneType = phoneType;
	}
	
	public PhoneDTO(String id, String phoneNumber, String phoneType, Calendar lastModified, Calendar createdAt) {
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
	
	public Phone toPhone() {
		return new Phone(StringUtils.isBlank(id) ? 0 : Integer.valueOf(id), phoneNumber, phoneType, lastModified, createdAt);
	}
	
	public static PhoneDTO from(Phone phone) {
		return new PhoneDTO(String.valueOf(phone.getId()), phone.getPhoneNumber(), phone.getPhoneType(), phone.getLastModified(), phone.getCreatedAt());
	}
	
	public int hashCode() {
		return Objects.hash(phoneNumber, phoneType);
	}
	
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PhoneDTO)) return false;
		
		PhoneDTO other = (PhoneDTO) o;
		return Objects.equals(phoneNumber, other.phoneNumber)
				&& Objects.equals(phoneType, other.phoneType);
	}
}
