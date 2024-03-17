package com.openclassrooms.paymybuddy.repository;

import org.springframework.data.repository.CrudRepository;

import com.openclassrooms.paymybuddy.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

}
