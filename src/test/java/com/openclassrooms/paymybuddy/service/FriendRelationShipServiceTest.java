package com.openclassrooms.paymybuddy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.paymybuddy.model.DBUser;
import com.openclassrooms.paymybuddy.model.FriendRelationShip;
import com.openclassrooms.paymybuddy.repository.DBUserRepository;

@ExtendWith(MockitoExtension.class)
public class FriendRelationShipServiceTest {

	@Mock
	private DBUserRepository dbuserRepository;

	@InjectMocks
	private FriendRelationShipService friendRelationShipService;

	private DBUser user1, user2;

	@BeforeEach
	void setUp() {
		user1 = new DBUser();
		user1.setId(1L);
		user1.setEmail("user1@example.com");

		user2 = new DBUser();
		user2.setId(2L);
		user2.setEmail("user2@example.com");
	}

	@Test
	void testCreateFriendRelationShipSuccess() throws Exception {
		// GIVEN
		when(dbuserRepository.findByEmail("user1@example.com")).thenReturn(Optional.of(user1));
		when(dbuserRepository.findByEmail("user2@example.com")).thenReturn(Optional.of(user2));
		// WHEN
		FriendRelationShip result = friendRelationShipService.createFriendRelationShip("user1@example.com",
				"user2@example.com");
		// THEN
		assertNotNull(result);
		assertEquals(user1.getId(), result.getUser().getId());
		assertEquals(user2.getId(), result.getFriend_id());

		verify(dbuserRepository).save(user1);
		verify(dbuserRepository).save(user2);
	}

	@Test
	void testCreateFriendRelationShipUserNotFound() {
		// GIVEN
		when(dbuserRepository.findByEmail("user1@example.com")).thenReturn(Optional.empty());
		// WHEN & THEN
		Exception exception = assertThrows(Exception.class, () -> {
			friendRelationShipService.createFriendRelationShip("user1@example.com", "user3@example.com");
		});

		String expectedMessage = "Creation of a new Friend RelationShip impossible";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
		verify(dbuserRepository, never()).save(any(DBUser.class));
	}

	@Test
	public void createFriendRelationShip_AlreadyFriends() {
		DBUser user1 = new DBUser();
		user1.setId((long) 1);
		user1.setEmail("user1@example.com");

		DBUser user2 = new DBUser();
		user2.setId((long) 1);
		user2.setEmail("user2@example.com");

		FriendRelationShip existingFriendship = new FriendRelationShip();
		existingFriendship.setFriend_id((long) 1);

		List<FriendRelationShip> friends = new ArrayList<>();
		friends.add(existingFriendship);

		user1.setFriends(friends);

		when(dbuserRepository.findByEmail("user1@example.com")).thenReturn(Optional.of(user1));
		when(dbuserRepository.findByEmail("user2@example.com")).thenReturn(Optional.of(user2));

		Exception exception = assertThrows(Exception.class, () -> {
			friendRelationShipService.createFriendRelationShip("user1@example.com", "user2@example.com");
		});

		assertEquals("already friend", exception.getMessage());
	}

	@Test
	public void createFriendRelationShip_UserNotFound() {
		when(dbuserRepository.findByEmail("user1@example.com")).thenReturn(Optional.of(user1));
		when(dbuserRepository.findByEmail("user2@example.com")).thenReturn(Optional.empty());

		Exception exception = assertThrows(Exception.class, () -> {
			friendRelationShipService.createFriendRelationShip("user1@example.com", "user2@example.com");
		});

		assertEquals("Friend not found", exception.getMessage());
	}
}
