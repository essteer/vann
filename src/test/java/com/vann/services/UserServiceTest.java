package com.vann.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

import com.vann.exceptions.*;
import com.vann.models.*;
import com.vann.repositories.*;


class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private CartRepo cartRepo;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOne() {
        User mockUser = new User("John Doe", "john.doe@example.com");
        when(userRepo.save(any(User.class))).thenReturn(mockUser);
        when(cartRepo.save(any(Cart.class))).thenReturn(new Cart());
    
        User savedUser = userService.createOne(mockUser);
    
        assertEquals(mockUser, savedUser);
        verify(userRepo, times(1)).save(mockUser);
        verify(cartRepo, times(1)).save(any(Cart.class));
    }

    @Test
    void testFindAllUsers() {
        User user1 = new User("John Doe", "john.doe@example.com");
        User user2 = new User("Jane Doe", "jane.doe@example.com");
    
        when(userRepo.findAll()).thenReturn(List.of(user1, user2));
    
        List<User> users = userService.findAllUsers();
    
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
        verify(userRepo, times(1)).findAll();
    }

    @Test
    void testFindUserById() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setName("John Doe");

        when(userRepo.findById(id)).thenReturn(Optional.of(user));
        User foundUser = userService.findUserById(id);

        assertEquals("John Doe", foundUser.getName());
        verify(userRepo, times(1)).findById(id);
    }

    @Test
    void testFindUserByIdNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepo.findById(userId)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> userService.findUserById(userId));
    }

    @Test
    void testFindUserByEmail() {
        String email = "john.doe@example.com";
        User user = new User();
        user.setEmail(email);
        user.setName("John Doe");
    
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
    
        User foundUser = userService.findUserByEmail(email);
    
        assertEquals("John Doe", foundUser.getName());
        assertEquals(email, foundUser.getEmail());
        verify(userRepo, times(1)).findByEmail(email);
    }

    @Test
    void testFindUserByEmailNotFound() {
        String email = "nonexistent@example.com";
        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> userService.findUserByEmail(email));
    }

    @Test
    void testSaveUser() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        when(userRepo.save(user)).thenReturn(user);

        User savedUser = userService.saveUser(user);

        assertNotNull(savedUser);
        assertEquals("John Doe", savedUser.getName());
        verify(userRepo, times(1)).save(user);
    }

    @Test
    void testUpdateUser() {
        UUID id = UUID.randomUUID();
        User existingUser = new User();
        existingUser.setName("John Doe");
        existingUser.setEmail("john.doe@example.com");
    
        User updatedUser = new User();
        updatedUser.setName("Jane Doe");
        updatedUser.setEmail("jane.doe@example.com");
    
        when(userRepo.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepo.save(existingUser)).thenReturn(updatedUser);
    
        User result = userService.updateUser(id, updatedUser);
    
        assertEquals("Jane Doe", result.getName());
        assertEquals("jane.doe@example.com", result.getEmail());
        verify(userRepo, times(1)).findById(id);
        verify(userRepo, times(1)).save(existingUser);
    }

    @Test
    void testDeleteUser() {
        UUID id = UUID.randomUUID();

        when(userRepo.existsById(id)).thenReturn(true);
        doNothing().when(userRepo).deleteById(id);

        userService.deleteUser(id);

        verify(userRepo, times(1)).deleteById(id);
    }
}
