package com.example.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class MessagesConfig {
	@Bean("messageSource")
	public MessageSource getMessageSource() {
		ReloadableResourceBundleMessageSource message = new ReloadableResourceBundleMessageSource();
		message.setBasenames("classpath:message");
		message.setDefaultEncoding("utf-8");
		return message;
	}
}