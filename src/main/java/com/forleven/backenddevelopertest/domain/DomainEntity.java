package com.forleven.backenddevelopertest.domain;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
public class DomainEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	protected int id;
	
	@Column(name="last_modification")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	protected Calendar lastModified = Calendar.getInstance();
	
	@Column(name="created_at")
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	protected Calendar createdAt = Calendar.getInstance();

	public DomainEntity() { }

	public DomainEntity(Calendar lastModified, Calendar createdAt) {
		this.lastModified = lastModified;
		this.createdAt = createdAt;
	}

	public DomainEntity(int id, Calendar lastModified, Calendar createdAt) {
		this.id = id;
		this.lastModified = lastModified;
		this.createdAt = createdAt;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
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
}
