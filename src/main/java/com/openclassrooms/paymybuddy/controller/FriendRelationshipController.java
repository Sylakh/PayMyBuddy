package com.openclassrooms.paymybuddy.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.paymybuddy.model.DBUser;
import com.openclassrooms.paymybuddy.model.FriendRelationShip;
import com.openclassrooms.paymybuddy.service.DBUserService;
import com.openclassrooms.paymybuddy.service.FriendRelationShipService;

@RestController
public class FriendRelationshipController {

	private static final Logger logger = LogManager.getLogger("FriendRelationShipController");

	@Autowired
	private FriendRelationShipService friendRelationShipService;

	@Autowired
	private DBUserService dbuserService;

	@PostMapping("/friendrelationship")
	public FriendRelationShip createRelation(@RequestParam String userEmail2) throws Exception {
		logger.info("Create a new Friend RelationShip in database");
		DBUser dbuser = dbuserService.getCurrentUser();
		System.out.println("user connected: " + dbuser.getEmail());
		return friendRelationShipService.createFriendRelationShip(dbuser.getEmail(), userEmail2);
	}

}
