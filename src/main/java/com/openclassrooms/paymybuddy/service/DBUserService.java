package com.openclassrooms.paymybuddy.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

}
