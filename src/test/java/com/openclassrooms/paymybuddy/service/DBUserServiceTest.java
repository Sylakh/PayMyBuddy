package com.openclassrooms.paymybuddy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.openclassrooms.paymybuddy.model.DBUser;
import com.openclassrooms.paymybuddy.repository.DBUserRepository;

@ExtendWith(MockitoExtension.class)
public class DBUserServiceTest {

	@Mock
	private DBUserRepository dbUserRepository;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@InjectMocks
	private DBUserService dbUserService;

	private String email = "test@example.com";
	private String password = "password";
	private String encodedPassword = "encodedPassword";

	@BeforeEach
	public void setup() {
		// Ici, on peut initialiser les objets nécessaires avant chaque test
	}

	@Test
	public void testCreateDBUser() {
		// GIVEN
		when(bCryptPasswordEncoder.encode(password)).thenReturn(encodedPassword);

		DBUser dbUser = new DBUser();
		dbUser.setEmail(email);
		dbUser.setPassword(encodedPassword); // Supposons que le service encode le mot de passe
		dbUser.setRole("USER");

		when(dbUserRepository.save(any(DBUser.class))).thenReturn(dbUser);
		// WHEN
		DBUser createdUser = dbUserService.createDBUser(email, password);
		// THEN
		assertEquals(email, createdUser.getEmail());
		assertEquals(encodedPassword, createdUser.getPassword());
		assertEquals("USER", createdUser.getRole());

		verify(bCryptPasswordEncoder, times(1)).encode(password);
		verify(dbUserRepository, times(1)).save(any(DBUser.class));
	}
}
