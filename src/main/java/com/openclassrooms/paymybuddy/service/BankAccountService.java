package com.openclassrooms.paymybuddy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.paymybuddy.model.BankAccount;
import com.openclassrooms.paymybuddy.model.DBUser;
import com.openclassrooms.paymybuddy.repository.DBUserRepository;

@Service
public class BankAccountService {

	private static final Logger logger = LogManager.getLogger("BankAccountService");

	@Autowired
	private DBUserRepository dbuserRepository;

	public BankAccount createBankAccount(String email, String bankName, String iban) {
		Optional<DBUser> optionalDBUser = dbuserRepository.findByEmail(email);
		logger.info("Create a new Bank Account in database");
		if (optionalDBUser.isPresent()) {
			DBUser dbuser = optionalDBUser.get();
			BankAccount bankAccount = new BankAccount();
			bankAccount.setBankName(bankName);
			bankAccount.setIban(iban);
			bankAccount.setUser(dbuser);
			List<BankAccount> bankAccounts = new ArrayList<>();
			bankAccounts = dbuser.getBankAccounts();
			bankAccounts.add(bankAccount);

			logger.info("Creation of a new Bank Account done");
			dbuserRepository.save(dbuser);
			return bankAccount;
		} else {
			logger.info("No user authenticated to create a new Bank Account in database");
			return null;
		}
	}

}
