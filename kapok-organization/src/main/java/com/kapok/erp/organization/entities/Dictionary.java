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

@Getter
@Setter
@Entity
@Table(name = "dictionary")
@EntityListeners(AuditingEntityListener.class)
public class Dictionary {
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

    @Size(max = 50)
    @NotNull
    @Column(name = "value", nullable = false, length = 50)
    private String value;

    @NotNull
    @Column(name = "type", nullable = false)
    private Byte type;

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

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

}
