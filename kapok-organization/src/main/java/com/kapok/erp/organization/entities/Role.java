package com.kapok.erp.organization.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "role")
@EntityListeners(AuditingEntityListener.class)
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Size(max = 50)
	@NotNull
	@Column(name = "code", nullable = false, length = 50)
	private String code;

	@Size(max = 50)
	@NotNull
	@Column(name = "name", nullable = false, length = 50)
	private String name;

	@NotNull
	@Column(name = "enabled", nullable = false)
	private Boolean enabled;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
	@CreatedDate
	@NotNull
	@Column(name = "createdDate", nullable = false)
	private Instant createdDate;

	@CreatedBy
	@Size(max = 20)
	@NotNull
	@Column(name = "createdBy", nullable = false, length = 20)
	private String createdBy;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
	@LastModifiedDate
	@NotNull
	@Column(name = "lastModifiedDate", nullable = false)
	private Instant lastModifiedDate;

	@LastModifiedBy
	@Size(max = 20)
	@NotNull
	@Column(name = "lastModifiedBy", nullable = false, length = 20)
	private String lastModifiedBy;

	@Version
	@NotNull
	@Column(name = "version", nullable = false)
	private Integer version;

	@Size(max = 500)
	@Column(name = "description", length = 500)
	private String description;

}
