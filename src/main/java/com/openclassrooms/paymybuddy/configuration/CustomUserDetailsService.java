package com.openclassrooms.paymybuddy.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.openclassrooms.paymybuddy.model.DBUser;
import com.openclassrooms.paymybuddy.repository.DBUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private DBUserRepository dbUserRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<DBUser> optionalUser = dbUserRepository.findByEmail(email);
		DBUser user = optionalUser.get();
		return new User(user.getEmail(), user.getPassword(), getGrantedAuthorities(user.getRole()));
	}

	private List<GrantedAuthority> getGrantedAuthorities(String role) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
		return authorities;
	}
}