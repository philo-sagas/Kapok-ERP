package com.kapok.erp.organization.inputs;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class RoleGrantParam implements Serializable {
	private Integer id;

	private String code;

	private String name;

	private Boolean enabled;

	private List<Integer> permIds;
}
