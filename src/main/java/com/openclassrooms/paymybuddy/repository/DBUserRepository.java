package com.openclassrooms.paymybuddy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.paymybuddy.model.DBUser;

@Repository
public interface DBUserRepository extends JpaRepository<DBUser, Long> {

	Optional<DBUser> findByEmail(String email);

	Optional<DBUser> findById(Long id);

	Optional<DBUser> findIdByNickName(String nickName);

}
