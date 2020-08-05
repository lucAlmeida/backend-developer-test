package com.forleven.backenddevelopertest.util;

import java.util.Objects;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:service.properties")
@Component
public class ServiceProperties {
	private ServiceProperties studentServiceProperties;
	
	private String exists;
	private String notExists;
	private String entityName;
	private String msgEnrollmentId;
	private String msgAlreadyExists;
	private String msgDoesNotExist;

	@Bean
	@ConfigurationProperties(prefix="student.service")
	public ServiceProperties studentServiceProperties() {
		if (Objects.isNull(studentServiceProperties)) {
			studentServiceProperties = new ServiceProperties();
		}
		return studentServiceProperties;
	}
	
	public String exists() {
		return exists;
	}
	
	public void setExists(String exists) {
		this.exists = exists;
	}

	public String notExists() {
		return notExists;
	}
	
	public void setNotExists(String notExists) {
		this.notExists = notExists;
	}

	public String entityName() {
		return entityName;
	}
	
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	
	public String msgEnrollmentId() {
		return msgEnrollmentId;
	}
	
	public void setMsgEnrollmentId(String msgEnrollmentId) {
		this.msgEnrollmentId = msgEnrollmentId;
	}
	
	public String msgAlreadyExists() {
		return msgAlreadyExists;
	}
	
	public void setMsgAlreadyExists(String msgAlreadyExists) {
		this.msgAlreadyExists = msgAlreadyExists;
	}
	
	public String msgDoesNotExist() {
		return msgDoesNotExist;
	}
	
	public void setMsgDoesNotExist(String msgDoesNotExist) {
		this.msgDoesNotExist = msgDoesNotExist;
	}
}
