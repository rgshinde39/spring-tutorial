package com.sb.tutorial.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired DataSource dataSource;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
			.csrf().disable()
		      .authorizeRequests()
		      .expressionHandler(webSecurityExpressionHandler())
		      .antMatchers("/public*").permitAll()
		      .antMatchers("/login*").permitAll()
		      .antMatchers("/logoutsuccess*").permitAll()
		      .mvcMatchers("/admin*").hasRole("ADMIN")
		      .mvcMatchers("/user*").hasRole("USER")
		      .mvcMatchers("/guest*").hasRole("GUEST")
		      .anyRequest().authenticated()
		      .and()
		      
		      //authentication based on a customizable login form with help of html form
		      .formLogin()
		      	.loginPage("/login")
		      .and()
		      
		      //if we dont specify any of the methods spring security will throw 403 AccessDeni
		      //simple browser popup will be used to authenticate user
//		      .httpBasic()

				.sessionManagement(
						session -> session
						//if incorrect JSESSION id passed user will be redirected to /invalid-session
						.invalidSessionUrl("/invalid-session")
						//user can login from 1 place only
//						.maximumSessions(1)
						.maximumSessions(-1)
						//login from other place will not affect the current login, in case of false current login will invalidated
						.maxSessionsPreventsLogin(true)
				)
		      	.logout(logout -> logout
		      			.deleteCookies("JSESSIONID")
		      			.logoutSuccessUrl("/logoutsuccess")
		      	)
		      	.rememberMe(remember -> remember
		      			.key("somerandomkey")
		      			.tokenValiditySeconds(15)
		      			.userDetailsService(users())
		      			.tokenRepository(persistentTokenRepository())
		      	)
		      	//anonymous is enabled by default to prevent NPE in SecurityContextHolder
		      	.anonymous()
		      	;
	}
	
	//different ways of password storage in spring security
	
	//InMemoryUserDetailsManager(C)
//	@Bean
//	public UserDetailsService users() {
//		// The builder will ensure the passwords are encoded before saving in memory
//		UserBuilder users = User.withDefaultPasswordEncoder();
//		UserDetails user = users
//			.username("user")
//			.password("password")
//			.roles("USER")
//			.build();
//		UserDetails admin = users
//			.username("admin")
//			.password("password")
//			.roles("USER", "ADMIN")
//			.build();
//		return new InMemoryUserDetailsManager(user, admin);
//	}
	
	//JdbcUserDetailsManager(C) - in this case real database credentials are used
	@Bean
	public UserDetailsService users() {
		
		//default schema tables - users, authorities, groups, group_authorities, group_members
		
		//here datasource is mandatory
		return new JdbcUserDetailsManager(dataSource);
	}
	
	@Bean
	public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
		return new SecurityEvaluationContextExtension();
	}
	
	@Bean
	public AuthenticationEventPublisher authenticationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
	}
	
	@Bean
	public RoleHierarchy roleHierarchy() {
	    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
	    String hierarchy = "ROLE_ADMIN > ROLE_USER \n ROLE_USER > ROLE_GUEST";
	    roleHierarchy.setHierarchy(hierarchy);
	    return roleHierarchy;
	}
	
	@Bean
	public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
	    DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
	    expressionHandler.setRoleHierarchy(roleHierarchy());
	    return expressionHandler;
	}
	
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new JdbcTokenRepositoryImpl();
		jdbcTokenRepositoryImpl.setDataSource(dataSource);
		return jdbcTokenRepositoryImpl;
	}
}
