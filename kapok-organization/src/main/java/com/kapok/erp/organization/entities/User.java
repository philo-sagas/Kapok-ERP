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
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Size(max = 20)
	@NotNull
	@Column(name = "subject", nullable = false, length = 20)
	private String subject;

	@Size(max = 60)
	@NotNull
	@Column(name = "password", nullable = false, length = 60)
	private String password;

	@NotNull
	@Column(name = "pwdChangeDate", nullable = false)
	private LocalDate pwdChangeDate;

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

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
	@Column(name = "unlockingDate")
	private Instant unlockingDate;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
	@Column(name = "disabledDate")
	private Instant disabledDate;

	@Size(max = 20)
	@Column(name = "username", length = 20)
	private String username;

	@Size(max = 20)
	@Column(name = "nickname", length = 20)
	private String nickname;

	@Size(max = 50)
	@Column(name = "picture", length = 50)
	private String picture;

	@Size(max = 20)
	@Column(name = "phoneNumber", length = 20)
	private String phoneNumber;

	@Column(name = "phoneNumberVerified")
	private Boolean phoneNumberVerified;

	@Size(max = 50)
	@Column(name = "email", length = 50)
	private String email;

	@Column(name = "emailVerified")
	private Boolean emailVerified;

	@Column(name = "gender")
	private Byte gender;

	@Column(name = "birthdate")
	private LocalDate birthdate;

	@Size(max = 100)
	@Column(name = "address", length = 100)
	private String address;

	@Size(max = 500)
	@Column(name = "description", length = 500)
	private String description;

	@Column(name = "roleId")
	private Integer roleId;

	@Column(name = "orgId")
	private Integer orgId;

}
