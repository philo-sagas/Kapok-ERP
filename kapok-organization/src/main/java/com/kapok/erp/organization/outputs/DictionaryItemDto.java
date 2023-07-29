package com.kapok.erp.organization.outputs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DictionaryItemDto {
	private String code;

	private String value;

	private Integer rank;
}
