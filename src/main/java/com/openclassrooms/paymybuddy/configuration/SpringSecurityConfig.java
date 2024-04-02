package com.openclassrooms.paymybuddy.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(auth -> {
			auth.requestMatchers("/admin").hasRole("ADMIN");
			auth.requestMatchers("/user").hasRole("USER");
			auth.requestMatchers("/dbuser").permitAll();
			auth.requestMatchers("/bankaccount").authenticated();
			auth.requestMatchers("/friendrelationship").authenticated();
			auth.requestMatchers("/transaction").authenticated();
			auth.anyRequest().authenticated();
		})

				.httpBasic(Customizer.withDefaults()) // Active l'authentification Basic Auth pour les requêtes
														// authentifiées
				.formLogin(Customizer.withDefaults()).csrf(AbstractHttpConfigurer::disable);

		return http.build();
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder)
			throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(customUserDetailsService)
				.passwordEncoder(bCryptPasswordEncoder);
		return authenticationManagerBuilder.build();
	}

}