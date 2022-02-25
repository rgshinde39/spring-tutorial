package com.sb.tutorial.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	//configured role hierarchy ROLE_ADMIN > ROLE_USER \n ROLE_USER > ROLE_GUEST
	
	@GetMapping("/admin")
	public String admin() {
		return "endpoint for ROLE_ADMIN";
	}
	
	@GetMapping("/user")
	public String user() {
		return "endpoint for ROLE_USER";
	}
	
	@GetMapping("/guest")
	public String guest() {
		return "endpoint for ROLE_GUEST";
	}
	
	@GetMapping("/user/{userId}")
	public String userId(@PathVariable String userId) {
		return "testing user id "+userId;
	}
	
	//based on our role hierarchy admin, user can access this method but guest can't
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/showDetails")
	public String showDetails() {
		return "showDetails";
	}
}
