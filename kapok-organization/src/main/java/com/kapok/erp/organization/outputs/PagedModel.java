package com.kapok.erp.organization.outputs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagedModel<E> extends ResultModel<List<E>> {
	private long total;

	public PagedModel(int status, String message, List<E> data, long total) {
		super(status, message, data);
		this.total = total;
	}

	public static <M> PagedModel<M> success(Page<M> page) {
		return new PagedModel<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), page.getContent(), page.getTotalElements());
	}
}
