package com.sb.tutorial.listener;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthenticationListener {

	@EventListener
	public void onSuccessfulAuthentication(AuthenticationSuccessEvent authenticationSuccessEvent) {
		Authentication authentication = authenticationSuccessEvent.getAuthentication();
		log.info("user {} was successfully authenticated at {}" , authentication.getPrincipal(), authenticationSuccessEvent.getTimestamp());
	}
	
	@EventListener
	public void onAuthenticationFailed(AbstractAuthenticationFailureEvent authenticationFailureEvent) {
		log.info("auth failed {}", authenticationFailureEvent.getException().getMessage());
	}
}
