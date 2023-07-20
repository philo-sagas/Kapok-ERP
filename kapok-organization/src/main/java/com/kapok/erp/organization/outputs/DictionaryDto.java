package com.kapok.erp.organization.outputs;

import com.kapok.erp.organization.entities.Dictionary;
import com.kapok.erp.organization.entities.DictionaryItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DictionaryDto extends Dictionary implements Serializable {
	private List<DictionaryItem> items;
}
