package com.kapok.erp.organization.config;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@RefreshScope
@EnableJpaAuditing
public class ApplicationConfig {
}
