package com.openclassrooms.paymybuddy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
import com.openclassrooms.paymybuddy.repository.DBUserRepository;

@ExtendWith(MockitoExtension.class)
public class BankAccountServiceTest {

	@Mock
	private DBUserRepository dbuserRepository;

	@InjectMocks
	private BankAccountService bankAccountService;

	private DBUser user;

	@BeforeEach
	void setUp() {
		user = new DBUser();
		user.setId(1L);
		user.setEmail("user@example.com");
	}

	@Test
	void testCreateBankAccountSuccess() {
		// GIVEN
		when(dbuserRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
		// WHEN
		BankAccount bankAccount = bankAccountService.createBankAccount("user@example.com", "BankName", "IBAN123");
		// THEN
		assertNotNull(bankAccount);
		assertEquals("BankName", bankAccount.getBankName());
		assertEquals("IBAN123", bankAccount.getIban());
		assertEquals(user, bankAccount.getUser());

		verify(dbuserRepository).findByEmail("user@example.com");
		verify(dbuserRepository).save(user);
	}

	@Test
	void testCreateBankAccountUserNotFound() {
		// GIVEN
		when(dbuserRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());
		// WHEN
		BankAccount bankAccount = bankAccountService.createBankAccount("nonexistent@example.com", "BankName",
				"IBAN123");
		// THEN
		assertNull(bankAccount);
		verify(dbuserRepository).findByEmail("nonexistent@example.com");
		verify(dbuserRepository, never()).save(any(DBUser.class));
	}
}
