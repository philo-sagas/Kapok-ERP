package com.kapok.erp.organization.outputs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TreeNode {
	private Integer id;

	private String name;

	private String path;

	private Boolean leaf;

	private List<TreeNode> children;
}
