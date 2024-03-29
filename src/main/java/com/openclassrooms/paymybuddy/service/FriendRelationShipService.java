package com.openclassrooms.paymybuddy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.paymybuddy.model.DBUser;
import com.openclassrooms.paymybuddy.model.FriendRelationShip;
import com.openclassrooms.paymybuddy.repository.DBUserRepository;

@Service
public class FriendRelationShipService {

	private static final Logger logger = LogManager.getLogger("FriendRelationShipService");

	@Autowired
	private DBUserRepository dbuserRepository;

	public FriendRelationShip createFriendRelationShip(String userEmail1, String userEmail2) throws Exception {
		logger.info("Create a new Friend RelationShip in database");
		Optional<DBUser> optionalDBUser1 = dbuserRepository.findByEmail(userEmail1);
		if (optionalDBUser1.isPresent()) {
			Optional<DBUser> optionalDBUser2 = dbuserRepository.findByEmail(userEmail2);
			if (optionalDBUser2.isPresent()) {
				DBUser dbuser1 = optionalDBUser1.get();
				DBUser dbuser2 = optionalDBUser2.get();
				List<FriendRelationShip> friends1 = new ArrayList<>();
				List<FriendRelationShip> friends2 = new ArrayList<>();
				friends1 = dbuser1.getFriends();
				friends2 = dbuser2.getFriends();
				FriendRelationShip friend1 = new FriendRelationShip();
				friend1.setUser(dbuser1);
				friend1.setFriend_id(dbuser2.getId());
				friends1.add(friend1);
				dbuser1.setFriends(friends1);
				FriendRelationShip friend2 = new FriendRelationShip();
				friend2.setUser(dbuser2);
				friend2.setFriend_id(dbuser1.getId());
				friends2.add(friend2);
				dbuser2.setFriends(friends2);
				dbuserRepository.save(dbuser1);
				dbuserRepository.save(dbuser2);
				logger.info("Creation of a new Friend RelationShip done");
				return friend1;
			}

		}
		logger.info("Creation of a new Friend RelationShip impossible");
		throw new Exception("Creation of a new Friend RelationShip impossible");
	}

}
