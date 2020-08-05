package com.forleven.backenddevelopertest.dto;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.forleven.backenddevelopertest.domain.DomainEntity;

import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties({"id", "lastModified", "createdAt"})
public class DomainEntityDTO {
	@ApiModelProperty(hidden=true)
	protected String id;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy@HH:mm:ss", timezone="America/Sao_Paulo")
	@ApiModelProperty(hidden=true)
	protected Calendar lastModified = Calendar.getInstance();
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy@HH:mm:ss", timezone="America/Sao_Paulo")
	@ApiModelProperty(hidden=true)
	protected Calendar createdAt = Calendar.getInstance();

	public DomainEntityDTO() { }

	public DomainEntityDTO(Calendar lastModified, Calendar createdAt) {
		this.lastModified = lastModified;
		this.createdAt = createdAt;
	}
	
	public DomainEntityDTO(String id, Calendar lastModified, Calendar createdAt) {
		this.id = id;
		this.lastModified = lastModified;
		this.createdAt = createdAt;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Calendar getLastModified() {
		return lastModified;
	}

	public void setLastModified(Calendar lastModified) {
		this.lastModified = lastModified;
	}

	public Calendar getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Calendar createdAt) {
		this.createdAt = createdAt;
	}
	
	public DomainEntity toDomainEntity() {
		return new DomainEntity(lastModified, createdAt);
	}
	
	public static DomainEntityDTO from(DomainEntity domainEntity) {
		return new DomainEntityDTO(domainEntity.getLastModified(), domainEntity.getCreatedAt());
	}
}
