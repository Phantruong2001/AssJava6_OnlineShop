package com.example.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.interceptor.AuthInterceptor;

@Configuration
public class AuthInterConfig implements WebMvcConfigurer {
	@Autowired
	AuthInterceptor auth;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(auth)
				.addPathPatterns(
						"/categorypage/**", 
						"/homepage/**", 
						"/invoicepage/**", 
						"/productpage/**",
						"/userpage/**",
						"/reportpage/**")
				.excludePathPatterns(
						"/css/**", 
						"/js/**", 
						"/images/**", 
						"/accountpage/login", 
						"/accountpage/logout",
						"/accountpage/confirm-password", 
						"/accountpage/forgot-password", 
						"/accountpage/register",
						"/shop/**");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String dirName = "uploads";
		Path uploadDir = Paths.get(dirName);
		String uploadPath = uploadDir.toFile().getAbsolutePath();
		if (dirName.startsWith("../"))
			dirName = dirName.replace("../", "");
		registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:/" + uploadPath + "/");
	}
}
