package com.openclassrooms.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.paymybuddy.model.DBUser;

@Repository
public interface DBUserRepository extends JpaRepository<DBUser, Long> {

	public DBUser findByEmail(String email);

}
