package com.openclassrooms.paymybuddy.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.paymybuddy.model.BankAccount;
import com.openclassrooms.paymybuddy.model.DBUser;
import com.openclassrooms.paymybuddy.service.BankAccountService;
import com.openclassrooms.paymybuddy.service.DBUserService;

@RestController
public class BankAccountController {

	private static final Logger logger = LogManager.getLogger("BankAccountController");

	@Autowired
	private BankAccountService bankAccountService;

	@Autowired
	private DBUserService dbuserService;

	@PostMapping("/bankaccount")
	public BankAccount addBankAccount(@RequestParam String bankName, @RequestParam String iban) throws Exception {
		logger.info("Create a new Bank Account in database for dbuser ");
		DBUser dbuser = dbuserService.getCurrentUser();
		System.out.println("user connected: " + dbuser.getEmail());
		return bankAccountService.createBankAccount(dbuser.getEmail(), bankName, iban);
	}

}
