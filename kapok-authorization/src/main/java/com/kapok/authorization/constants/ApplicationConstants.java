package com.kapok.authorization.constants;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "kapok.authorization")
public class ApplicationConstants {
	private List<String> corsRedirectUris;
}
