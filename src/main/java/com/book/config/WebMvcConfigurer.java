package com.book.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
public class WebMvcConfigurer implements org.springframework.web.servlet.config.annotation.WebMvcConfigurer {
	@Autowired
	private LoginInterceptor loginInterceptor;
	@Autowired
	private VMConfig config;

	// 开发时的 跨域配置
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping(config.originMapping)
				.allowedOrigins(config.origin)
				.allowedMethods(config.originMethods)
				.allowCredentials(config.originCredentials)
				.allowedHeaders(config.originHeaders)
				.maxAge(3600);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginInterceptor);
	}
}