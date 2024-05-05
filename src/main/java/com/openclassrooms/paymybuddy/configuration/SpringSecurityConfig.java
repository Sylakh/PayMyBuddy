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
			auth.requestMatchers("/login").permitAll();
			auth.requestMatchers("/dbuser").permitAll();
			auth.requestMatchers("/bankaccount").authenticated();
			auth.requestMatchers("/friendrelationship").authenticated();
			auth.requestMatchers("/transaction").authenticated();
			auth.requestMatchers("/transfert").authenticated();
			auth.requestMatchers("/contact").authenticated();
			auth.anyRequest().authenticated();
		})

				.httpBasic(Customizer.withDefaults()) // Active l'authentification Basic Auth pour les requêtes
														// authentifiées
				.formLogin(form -> form.loginPage("/login") // Utilisez "/login" pour l'URL de la page de login

						.loginProcessingUrl("/login") // URL de traitement du formulaire de connexion
						.usernameParameter("email") // Paramètre de nom d'utilisateur attendu dans le
													// formulaire de login
						.defaultSuccessUrl("/transfert", true) // Page de redirection après une connexion réussie
						.permitAll() // Autoriser tous les utilisateurs à accéder à la page de login
				).rememberMe(rememberMe -> rememberMe.key("uniqueAndSecret"))
				.logout(logout -> logout.logoutUrl("/logout") // URL de déconnexion
						.logoutSuccessUrl("/login?logout") // Redirigez ici après la déconnexion
						.permitAll())
				.csrf(AbstractHttpConfigurer::disable); // Désactiver CSRF pour éviter les conflits

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