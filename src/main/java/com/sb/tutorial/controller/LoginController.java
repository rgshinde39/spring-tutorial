package com.sb.tutorial.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
	
	@GetMapping("/public")
	public @ResponseBody String publicUrl() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(authentication);
		
		if(authentication instanceof AnonymousAuthenticationToken) {
			return "user is anonymous";
		} else {
			return "user is known";
		}
	}
	
	@GetMapping("/invalid-session")
	public String invalidSession() {
		return "invalid-session";
	}
	
	@GetMapping("/logoutsuccess")
	public @ResponseBody String logoutSuccess() {
		return "logout success";
	}
}
