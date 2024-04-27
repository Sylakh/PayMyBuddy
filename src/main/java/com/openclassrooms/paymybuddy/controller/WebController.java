package com.openclassrooms.paymybuddy.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.openclassrooms.paymybuddy.model.DBUser;
import com.openclassrooms.paymybuddy.service.DBUserService;
import com.openclassrooms.paymybuddy.service.FriendRelationShipService;

@Controller
public class WebController {

	private static final Logger logger = LogManager.getLogger("webController");

	@Autowired
	private FriendRelationShipService friendRelationShipService;

	@Autowired
	private DBUserService dbuserService;

	@GetMapping("/login")
	public String loginPage() {

		return "login";
	}

	@GetMapping("/transfert")
	public String transfertPage() {

		return "transfert";
	}

	@GetMapping("/")
	public String rememberMeResultPage() {

		return "transfert";
	}

	@GetMapping("/contact")
	public String contactPage() {

		return "contact";
	}

	@PostMapping("/addcontact")
	public String addContact(@RequestParam String connection) throws Exception {
		logger.info("Create a new Friend RelationShip in database");
		DBUser dbuser = dbuserService.getCurrentUser();
		System.out.println("user connected: " + dbuser.getEmail());
		friendRelationShipService.createFriendRelationShip(dbuser.getEmail(), connection);
		return "transfert";
	}
}
