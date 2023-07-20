package com.kapok.erp.organization.constants;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "kapok.organization")
public class ApplicationConstants {
	public static final String PLACEHOLDER = "🌟"; // ✨

	public interface Dictionary {
		Byte typeSingle = 1;
		Byte typeMulti = 2;
	}
}
