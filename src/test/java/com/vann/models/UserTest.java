package com.vann.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;


public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("John Doe", "john.doe@example.com");
    }

    @Test
    public void testUserDefaultConstructor() {
        User defaultUser = new User();
        assertNull(defaultUser.getName(), "User name should be null");
        assertNull(defaultUser.getEmail(), "User email should be null");
    }

    @Test
    public void testUserParameterizedConstructor() {
        assertEquals("John Doe", user.getName(), "User name should match");
        assertEquals("john.doe@example.com", user.getEmail(), "User email should match");
    }

    @Test
    public void testSettersAndGetters() {
        user.setName("Jane Doe");
        user.setEmail("jane.doe@example.com");

        assertEquals("Jane Doe", user.getName(), "User name should match");
        assertEquals("jane.doe@example.com", user.getEmail(), "User email should match");
    }

    @Test
    public void testToString() {
        String expectedString = "User [id=" + user.getId() + 
                                ", name=John Doe, email=john.doe@example.com]";
        assertEquals(expectedString, user.toString(), "toString should match");
    }
}
