package com.openclassrooms.paymybuddy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.paymybuddy.model.BankAccount;
import com.openclassrooms.paymybuddy.model.DBUser;
import com.openclassrooms.paymybuddy.model.FriendRelationShip;
import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.repository.BankAccountRepository;
import com.openclassrooms.paymybuddy.repository.DBUserRepository;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

	@Mock
	private DBUserRepository dbuserRepository;

	@Mock
	private BankAccountRepository bankAccountRepository;

	@InjectMocks
	private TransactionService transactionService;

	private DBUser emitter, receiver;
	private BankAccount bankAccount;

	@BeforeEach
	void setUp() {
		emitter = new DBUser();
		emitter.setId(1L);
		emitter.setEmail("emitter@example.com");
		emitter.setBalance(1000.0);

		receiver = new DBUser();
		receiver.setId(2L);
		receiver.setEmail("receiver@example.com");

		bankAccount = new BankAccount();
		bankAccount.setId(1L);
		bankAccount.setBankName("BankName");
		bankAccount.setIban("IBAN123");
		bankAccount.setUser(emitter);
	}

	@Test
	void createTransactionBetweenFriendsSuccess() throws Exception {
		// GIVEN
		when(dbuserRepository.findByEmail(emitter.getEmail())).thenReturn(Optional.of(emitter));
		when(dbuserRepository.findById(receiver.getId())).thenReturn(Optional.of(receiver));

		FriendRelationShip friendRelationShip = new FriendRelationShip();
		friendRelationShip.setUser(emitter);
		friendRelationShip.setFriend_id(receiver.getId());
		emitter.getFriends().add(friendRelationShip);
		// WHEN
		Transaction transaction = transactionService.createTransaction(emitter.getEmail(), receiver.getId(), 100.0,
				false, "Test transaction");
		// THEN
		assertNotNull(transaction);
		assertEquals(900.0, emitter.getBalance());
		assertEquals(100.0, receiver.getBalance());
		assertFalse(transaction.isWithdraw());
		assertEquals("Test transaction", transaction.getDescription());

		verify(dbuserRepository).save(emitter);
		verify(dbuserRepository).save(receiver);
	}

	// Test pour le cas de succès de la transaction de retrait
	@Test
	void createWithdrawTransactionSuccess() throws Exception {
		when(dbuserRepository.findByEmail(emitter.getEmail())).thenReturn(Optional.of(emitter));
		when(bankAccountRepository.findById(bankAccount.getId())).thenReturn(Optional.of(bankAccount));

		Transaction transaction = transactionService.createTransaction(emitter.getEmail(), bankAccount.getId(), 100.0,
				true, "Withdraw");

		assertNotNull(transaction);
		assertTrue(transaction.isWithdraw());
		assertEquals(900.0, emitter.getBalance());
		assertEquals("Withdraw", transaction.getDescription());

		verify(dbuserRepository).save(emitter);
	}

	// Test pour l'échec de la transaction lorsque l'émetteur n'existe pas
	@Test
	void transactionFailWhenEmitterDoesNotExist() {
		when(dbuserRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

		Exception exception = assertThrows(Exception.class, () -> transactionService
				.createTransaction("nonexistent@example.com", receiver.getId(), 100.0, false, "Fail"));

		assertEquals("emitter of the transaction not found", exception.getMessage());
	}

	// Ajoutez d'autres méthodes de test ici pour couvrir différents scénarios
	// Pour éviter la redondance, ajustez les mocks et les vérifications en fonction
	// de chaque scénario

	// Test de l'échec de la transaction lorsque le destinataire n'est pas un ami
	@Test
	void transactionFailWhenReceiverIsNotAFriend() throws Exception {
		when(dbuserRepository.findByEmail(emitter.getEmail())).thenReturn(Optional.of(emitter));
		when(dbuserRepository.findById(receiver.getId())).thenReturn(Optional.of(receiver));
		// Ne pas ajouter receiver comme ami

		Exception exception = assertThrows(Exception.class, () -> transactionService
				.createTransaction(emitter.getEmail(), receiver.getId(), 100.0, false, "Not a friend"));

		assertEquals("receiver not friend with emitter", exception.getMessage());
	}

	// Test pour l'échec de la transaction lorsque l'émetteur n'a pas suffisamment
	// de fonds
	@Test
	void transactionFailWhenNotEnoughFunds() throws Exception {
		emitter.setBalance(50.0); // L'émetteur a seulement 50 dans le solde
		when(dbuserRepository.findByEmail(emitter.getEmail())).thenReturn(Optional.of(emitter));

		Exception exception = assertThrows(Exception.class, () -> transactionService
				.createTransaction(emitter.getEmail(), receiver.getId(), 100.0, false, "Not enough funds"));

		assertEquals("not enough fund for this transaction", exception.getMessage());
	}
}
