package com.sb.tutorial.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CheckUserRole {

	public boolean checkAdminRole(Authentication authentication, HttpServletRequest request) {
		//some logic that makes a decision whether to allow currently authenticated user to access the request
		log.info("checking user admin role to access {}", request.getRequestURI());
		
		boolean result = authentication.getAuthorities().stream().filter(g -> g.getAuthority().equals("ROLE_ADMIN")).findAny().isPresent();
		if(result) {
			log.info("access granted as user has authority of ROLE_ADMIN");
		} else {
			log.info("access denied as user does not have authority of ROLE_ADMIN");
		}
		return result;
	}
	
	public boolean checkUserId(Authentication authentication, String userId) {
		log.info("user id received {}", userId);
		//using the user id we can make decisions whether to allow user or not
		return true;
	}
}
