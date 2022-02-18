package com.sb.tutorial.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired DataSource dataSource;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
			.csrf().disable()
		      .authorizeRequests()
		      .antMatchers("/login*").permitAll()
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
		      	)
		      	.rememberMe(remember -> remember
		      			.key("somerandomkey")
		      			.tokenValiditySeconds(15)
		      			.userDetailsService(users())
		      			.tokenRepository(persistentTokenRepository())
		      	)
		      	
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
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new JdbcTokenRepositoryImpl();
		jdbcTokenRepositoryImpl.setDataSource(dataSource);
		return jdbcTokenRepositoryImpl;
	}
}
