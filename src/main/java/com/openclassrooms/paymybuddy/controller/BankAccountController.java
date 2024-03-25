package com.openclassrooms.paymybuddy.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.paymybuddy.model.BankAccount;
import com.openclassrooms.paymybuddy.service.BankAccountService;

@RestController
public class BankAccountController {

	private static final Logger logger = LogManager.getLogger("BankAccountController");

	@Autowired
	private BankAccountService bankAccountService;

	@PostMapping("/bankaccount")
	public BankAccount addBankAccount(@RequestParam String email, @RequestParam String bankName,
			@RequestParam String iban) {
		logger.info("Create a new Bank Account in database for dbuser ");
		return bankAccountService.createBankAccount(email, bankName, iban);
	}

}
