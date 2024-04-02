package com.openclassrooms.paymybuddy.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.paymybuddy.model.DBUser;
import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.service.DBUserService;
import com.openclassrooms.paymybuddy.service.TransactionService;

@RestController
public class TransactionController {

	private static final Logger logger = LogManager.getLogger("DBUserController");

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private DBUserService dbuserService;

	@PostMapping("/transaction")
	public Transaction createTransaction(@RequestParam Long receiverId, @RequestParam double amount,
			@RequestParam boolean withdraw, @RequestParam String description) throws Exception {
		logger.info("Create a new transaction in database");
		DBUser dbuser = dbuserService.getCurrentUser();
		System.out.println("user connected: " + dbuser.getEmail());
		return transactionService.createTransaction(dbuser.getEmail(), receiverId, amount, withdraw, description);
	}

}
