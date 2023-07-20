package com.kapok.erp.organization.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class RolePermissionId implements Serializable {
	private static final long serialVersionUID = 275670841463271058L;
	@NotNull
	@Column(name = "roleId", nullable = false)
	private Integer roleId;

	@NotNull
	@Column(name = "permId", nullable = false)
	private Integer permId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		RolePermissionId entity = (RolePermissionId) o;
		return Objects.equals(this.roleId, entity.roleId) &&
				Objects.equals(this.permId, entity.permId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(roleId, permId);
	}

}
