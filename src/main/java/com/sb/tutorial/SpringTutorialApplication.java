package com.sb.tutorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

@SpringBootApplication
@ServletComponentScan
public class SpringTutorialApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringTutorialApplication.class, args);
		
		//default password encoder i.e. NoOpPasswordEncoder, it does hashes the password but plane text is still stored in memory
		//so only use this approach for sample or demo applications not for production
//		UserDetails user = User.withDefaultPasswordEncoder()
//				.username("user")
//				.password("password")
//				.roles("user")
//				.build();
//		System.out.println(user.getPassword());
		//{bcrypt}$2a$10$.3qiRP5dTr3H6bflxkCPDey5KpNWRILbRArf5pStHQ2PYC85O15Iq
		
		//createTestAuthenticatedUser();
	}
	
	@Bean
	public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
		return new SecurityEvaluationContextExtension();
	}
	
	public static void createTestAuthenticatedUser() {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		
		Authentication authentication = new TestingAuthenticationToken("rahul", "spring123", "ROLE_USER");
		context.setAuthentication(authentication);
		
		//basically here pretended that user rahul has logged in and ROLE_USER authority
		SecurityContextHolder.setContext(context);
		
		
		//obtain context details as following
		
		//SecurityContextHolder holds SecurityContext
		context = SecurityContextHolder.getContext();
		
		//the SecurityContext holds currently authenticated user details i.e. Authentication
		authentication = context.getAuthentication();
		
		//Authentication holds specific details about logged in user as following
		String username = authentication.getName();
		Object credentials = authentication.getCredentials();
		Object principal = authentication.getPrincipal();
		Object authorities = authentication.getAuthorities();
		
		System.out.println(username);
		System.out.println(credentials);
		System.out.println(principal);
		System.out.println(authorities);
	}
}
