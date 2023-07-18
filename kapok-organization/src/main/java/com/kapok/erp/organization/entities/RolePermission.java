package com.kapok.erp.organization.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rolePermission")
public class RolePermission {
    @EmbeddedId
    private RolePermissionId id;

    //TODO [JPA Buddy] generate columns from DB
}