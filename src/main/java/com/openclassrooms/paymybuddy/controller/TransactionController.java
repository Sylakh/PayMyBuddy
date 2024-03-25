package com.openclassrooms.paymybuddy.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.service.TransactionService;

@RestController
public class TransactionController {

	private static final Logger logger = LogManager.getLogger("DBUserController");

	@Autowired
	private TransactionService transactionService;

	@PostMapping("/transaction")
	public Transaction createTransaction(@RequestParam String emitterEmail, @RequestParam Long receiverId,
			@RequestParam double amount, @RequestParam boolean withdraw, @RequestParam String description) {
		logger.info("Create a new transaction in database");
		return transactionService.createTransaction(emitterEmail, receiverId, amount, withdraw, description);
	}

}
