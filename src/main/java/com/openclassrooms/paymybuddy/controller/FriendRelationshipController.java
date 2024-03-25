package com.openclassrooms.paymybuddy.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.paymybuddy.model.FriendRelationShip;
import com.openclassrooms.paymybuddy.service.FriendRelationShipService;

@RestController
public class FriendRelationshipController {

	private static final Logger logger = LogManager.getLogger("FriendRelationShipController");

	@Autowired
	private FriendRelationShipService friendRelationShipService;

	@PostMapping("/friendrelationship")
	public FriendRelationShip createRelation(@RequestParam String userEmail1, @RequestParam String userEmail2) {
		logger.info("Create a new Friend RelationShip in database");
		return friendRelationShipService.createFriendRelationShip(userEmail1, userEmail2);
	}

}
