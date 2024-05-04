package com.openclassrooms.paymybuddy.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.paymybuddy.DTO.TransactionDTO;
import com.openclassrooms.paymybuddy.model.BankAccount;
import com.openclassrooms.paymybuddy.model.DBUser;
import com.openclassrooms.paymybuddy.model.FriendRelationShip;
import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.repository.BankAccountRepository;
import com.openclassrooms.paymybuddy.repository.DBUserRepository;

@Service
public class TransactionService {

	private static final Logger logger = LogManager.getLogger("TransactionService");

	@Autowired
	private DBUserRepository dbuserRepository;

	@Autowired
	private BankAccountRepository bankAccountRepository;

	@Transactional
	public Transaction createTransaction(String emitterEmail, Long receiverId, double amount, boolean withdraw,
			String description) throws Exception {
		logger.info("Create a new transaction in database");

		// trouver le dbuser emitter

		// si non withdraw alors
		// verifier que le dbuser possede les fonds
		// trouver le dbuser receiver
		// verifier qu'il fait partie des amis
		// attribuer les fonds
		// sauvegarder la transaction et le dbuser emitter
		// sauvegarder le dbuser receiver

		// si withdraw
		// verifier que le dbuser possede les fonds
		// trouver le bankaccount
		// verifier qu'il fait parti des bankaccount du dbuser
		// retirer les fonds du dbuser
		// sauvegarder la transaction et le dbuser

		Optional<DBUser> optionalEmitter = dbuserRepository.findByEmail(emitterEmail);
		if (optionalEmitter.isPresent()) {
			DBUser emitter = optionalEmitter.get();
			if (emitter.getBalance() > amount) {
				if (!withdraw) {
					Optional<DBUser> optionalReceiver = dbuserRepository.findById(receiverId);
					if (optionalReceiver.isPresent()) {
						DBUser receiver = optionalReceiver.get();
						Boolean isFriend = false;
						for (FriendRelationShip friend : emitter.getFriends()) {
							if (friend.getFriend_id() == receiver.getId()) {
								isFriend = true;
								break;
							}
						}
						if (isFriend == true) {
							emitter.setBalance(emitter.getBalance() - amount);
							receiver.setBalance(receiver.getBalance() + amount);
							List<Transaction> transactions = new ArrayList<>();
							Transaction transaction = new Transaction();
							transactions = emitter.getTransactions();
							transaction.setUser(emitter);
							transaction.setReceiverId(receiver.getId());
							transaction.setWithdraw(withdraw);
							transaction.setAmount(amount);
							transaction.setDescription(description);
							transactions.add(transaction);
							emitter.setTransactions(transactions);
							dbuserRepository.save(emitter);
							dbuserRepository.save(receiver);
							logger.info("new transaction done");
							return transaction;
						} else {
							logger.info("receiver not friend with emitter");
							throw new Exception("receiver not friend with emitter");
						}

					} else {
						logger.info("receiver not found");
						throw new Exception("receiver not found");
					}

				} else {
					Optional<BankAccount> optionalBankAccount = bankAccountRepository.findById(receiverId);
					if (optionalBankAccount.isPresent()) {
						BankAccount bankAccount = optionalBankAccount.get();
						if (bankAccount.getUser().getEmail().equals(emitter.getEmail())) {
							emitter.setBalance(emitter.getBalance() - amount);
							List<Transaction> transactions = new ArrayList<>();
							Transaction transaction = new Transaction();
							transactions = emitter.getTransactions();
							transaction.setUser(emitter);
							transaction.setReceiverId(bankAccount.getId());
							transaction.setWithdraw(withdraw);
							transaction.setAmount(amount);
							transaction.setDescription(description);
							transactions.add(transaction);
							emitter.setTransactions(transactions);
							dbuserRepository.save(emitter);
							logger.info("new withdraw done");
							return transaction;
						} else {
							logger.info("Bank account not belong to emitter");
							throw new Exception("Bank account not belong to emitter");
						}

					} else {
						logger.info("Bank account not found");
						throw new Exception("Bank account not found");
					}
				}
			} else {
				logger.info("not enough fund for this transaction");
				throw new Exception("not enough fund for this transaction");
			}
		} else {
			logger.info("emitter not found");
			throw new Exception("emitter of the transaction not found");
		}

	}

	public Page<TransactionDTO> findPaginated(Pageable pageable, List<TransactionDTO> transactionsDTO) {
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		List<TransactionDTO> list;

		if (transactionsDTO.size() < startItem) {
			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, transactionsDTO.size());
			list = transactionsDTO.subList(startItem, toIndex);
		}
		Page<TransactionDTO> transactionsPage = new PageImpl<TransactionDTO>(list,
				PageRequest.of(currentPage, pageSize), transactionsDTO.size());
		return transactionsPage;
	}

}
