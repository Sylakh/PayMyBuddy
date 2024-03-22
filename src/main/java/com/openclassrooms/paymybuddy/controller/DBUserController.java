package com.openclassrooms.paymybuddy.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.paymybuddy.model.DBUser;
import com.openclassrooms.paymybuddy.service.DBUserService;

@RestController
public class DBUserController {

	private static final Logger logger = LogManager.getLogger("DBUserController");

	@Autowired
	private DBUserService dbuserService;

	@PostMapping("/dbuser")
	public DBUser createDBUser(@RequestParam String email, @RequestParam String password) {
		logger.info("Create a new user in database");
		return dbuserService.createDBUser(email, password);
	}

}
