package com.openclassrooms.paymybuddy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
	private SecurityContext securityContext;
	private Authentication authentication;

	@BeforeEach
	public void setup() {
		securityContext = Mockito.mock(SecurityContext.class);
		authentication = Mockito.mock(Authentication.class);
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

	@Test
	void testGetCurrentUser_Authenticated() throws Exception {
		// Arrange
		String email = "user@example.com";
		DBUser expectedUser = new DBUser();
		expectedUser.setEmail(email);

		Mockito.when(authentication.getName()).thenReturn(email);
		Mockito.when(authentication.isAuthenticated()).thenReturn(true);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		Mockito.when(dbUserRepository.findByEmail(email)).thenReturn(Optional.of(expectedUser));

		// Act
		DBUser user = dbUserService.getCurrentUser();

		// Assert
		assertEquals(expectedUser, user);
	}

	@Test
	void testGetCurrentUser_NotAuthenticated() {
		// Arrange
		Mockito.when(authentication.isAuthenticated()).thenReturn(false);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		// Act & Assert
		Exception exception = assertThrows(Exception.class, () -> dbUserService.getCurrentUser());
		assertEquals("User is not authenticated or does not exist", exception.getMessage());
	}

	@Test
	void testGetUser_Existing() throws Exception {
		// Arrange
		String email = "user@example.com";
		DBUser expectedUser = new DBUser();
		expectedUser.setEmail(email);

		Mockito.when(dbUserRepository.findByEmail(email)).thenReturn(Optional.of(expectedUser));

		// Act
		DBUser user = dbUserService.getUser(email);

		// Assert
		assertEquals(expectedUser, user);
	}

	@Test
	void testGetUser_NonExistent() {
		// Arrange
		String email = "nonexistent@example.com";
		Mockito.when(dbUserRepository.findByEmail(email)).thenReturn(Optional.empty());

		// Act & Assert
		Exception exception = assertThrows(Exception.class, () -> dbUserService.getUser(email));
		assertEquals("User does not exist (email provided: " + email + ")", exception.getMessage());
	}

	@Test
	void testFindNicknameById_ValidUser() {
		// Arrange
		Long userId = 1L;
		String expectedNickname = "john_doe";
		DBUser dbUser = new DBUser();
		dbUser.setId(userId);
		dbUser.setNickName(expectedNickname);

		when(dbUserRepository.findById(userId)).thenReturn(Optional.of(dbUser));

		// Act
		String nickname = dbUserService.findNicknameById(userId);

		// Assert
		assertEquals(expectedNickname, nickname);
	}

	@Test
	void testFindNicknameById_UnknownNickname() {
		// Arrange
		Long userId = 2L;
		DBUser dbUser = new DBUser();
		dbUser.setId(userId);
		dbUser.setNickName(null);

		when(dbUserRepository.findById(userId)).thenReturn(Optional.of(dbUser));

		// Act
		String nickname = dbUserService.findNicknameById(userId);

		// Assert
		assertEquals("unknown", nickname);
	}

	@Test
	void testFindNicknameById_NonExistentUser() {
		// Arrange
		Long userId = 3L;

		when(dbUserRepository.findById(userId)).thenReturn(Optional.empty());

		// Act
		String nickname = dbUserService.findNicknameById(userId);

		// Assert
		assertEquals("customer not available", nickname);
	}

	@Test
	void testFindIdByNickName_ValidNickname() {
		// Arrange
		String nickname = "jane_doe";
		Long expectedUserId = 4L;
		DBUser dbUser = new DBUser();
		dbUser.setId(expectedUserId);
		dbUser.setNickName(nickname);

		when(dbUserRepository.findIdByNickName(nickname)).thenReturn(Optional.of(dbUser));

		// Act
		Long userId = dbUserService.findIdByNickName(nickname);

		// Assert
		assertEquals(expectedUserId, userId);
	}

	@Test
	void testFindIdByNickName_NonExistentNickname() {
		// Arrange
		String nickname = "nonexistent_nick";

		when(dbUserRepository.findIdByNickName(nickname)).thenReturn(Optional.empty());

		// Act
		Long userId = dbUserService.findIdByNickName(nickname);

		// Assert
		assertEquals(-1, userId);
	}
}
