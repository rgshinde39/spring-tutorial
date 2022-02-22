package com.sb.tutorial.controller;

import org.springframework.web.bind.annotation.GetMapping;
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
}
