package com.openclassrooms.paymybuddy.repository;

import org.springframework.data.repository.CrudRepository;

import com.openclassrooms.paymybuddy.model.BankAccount;

public interface BankAccountRepository extends CrudRepository<BankAccount, Long> {

}
