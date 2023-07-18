package com.kapok.erp.organization.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
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

    @Size(max = 50)
    @NotNull
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Size(max = 102)
    @NotNull
    @Column(name = "password", nullable = false, length = 102)
    private String password;

    @Size(max = 15)
    @NotNull
    @Column(name = "salt", nullable = false, length = 15)
    private String salt;

    @NotNull
    @Column(name = "pwdChangeDate", nullable = false)
    private LocalDate pwdChangeDate;

    @NotNull
    @Column(name = "status", nullable = false)
    private Byte status;

    @CreatedDate
    @NotNull
    @Column(name = "createdDate", nullable = false)
    private Instant createdDate;

    @CreatedBy
	@NotNull
	@Column(name = "createdBy", nullable = false, length = 50)
    private String createdBy;

    @Version
    @NotNull
    @Column(name = "version", nullable = false)
    private Integer version;

    @Size(max = 20)
    @Column(name = "mobile", length = 20)
    private String mobile;

    @Column(name = "mobileVerified")
    private Boolean mobileVerified;

    @Size(max = 50)
    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "emailVerified")
    private Boolean emailVerified;

    @Size(max = 20)
    @Column(name = "nickname", length = 20)
    private String nickname;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "roleId")
    private Integer roleId;

    @Column(name = "orgId")
    private Integer orgId;

}
