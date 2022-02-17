package com.sb.tutorial.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	//spring security is configured to redirect unauthenticated user at this end point
	@GetMapping("/login")
	public String getLogin() {
		return "login";
	}
	
	@GetMapping("/private")
	public String privatePage() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(authentication);
		
		return "private";
	}
	
	@GetMapping("/invalid-session")
	public String invalidSession() {
		return "invalid-session";
	}
}
