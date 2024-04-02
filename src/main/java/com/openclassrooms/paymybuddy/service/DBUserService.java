package com.openclassrooms.paymybuddy.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassrooms.paymybuddy.model.DBUser;
import com.openclassrooms.paymybuddy.repository.DBUserRepository;

@Service
public class DBUserService {

	private static final Logger logger = LogManager.getLogger("DBUserService");

	@Autowired
	private DBUserRepository dbuserRepository;

	@Autowired
	private BCryptPasswordEncoder bscriptPasswordEncoder;

	public DBUser createDBUser(String email, String password) {
		DBUser dbuser = new DBUser();
		dbuser.setEmail(email);
		dbuser.setPassword(bscriptPasswordEncoder.encode(password));
		dbuser.setRole("USER");
		logger.info("Create a new user in database");
		return dbuserRepository.save(dbuser);
	}

	public DBUser getCurrentUser() throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated()) {
			String email = auth.getName();
			return getUser(email);
		} else {
			throw new Exception("User is not authenticated or does not exist");
		}
	}

	public DBUser getUser(String email) throws Exception {
		Optional<DBUser> optionalUser = dbuserRepository.findByEmail(email);
		if (optionalUser.isPresent()) {
			return optionalUser.get();
		} else {
			throw new Exception("User does not exist (email provided: " + email + ")");
		}
	}
}
