package com.openclassrooms.paymybuddy.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.paymybuddy.model.FriendRelationShip;

@Repository
public interface FirendRelationShipRepository extends CrudRepository<FriendRelationShip, Long> {

}
