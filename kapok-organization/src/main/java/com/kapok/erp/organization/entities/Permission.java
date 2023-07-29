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
@Table(name = "permission")
@EntityListeners(AuditingEntityListener.class)
public class Permission {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	@NotNull
	@Column(name = "pid", nullable = false)
	private Integer pid;

	@Size(max = 50)
	@NotNull
	@Column(name = "code", nullable = false, length = 50)
	private String code;

	@Size(max = 254)
	@NotNull
	@Column(name = "pCode", nullable = false, length = 254)
	private String pCode;

	@Size(max = 50)
	@NotNull
	@Column(name = "name", nullable = false, length = 50)
	private String name;

	@NotNull
	@Column(name = "type", nullable = false)
	private Byte type;

	@NotNull
	@Column(name = "rank", nullable = false)
	private Integer rank;

	@NotNull
	@Column(name = "leaf", nullable = false)
	private Boolean leaf;

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
