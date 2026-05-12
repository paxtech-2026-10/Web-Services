package com.paxtech.utime.platform.iam.domain.model.aggregates;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldCreateUserWithEmailAndPassword() {
        User user = new User("test@paxtech.com", "securePassword123");

        assertEquals("test@paxtech.com", user.getEmail());
        assertEquals("securePassword123", user.getPassword());
    }

    @Test
    void shouldCreateUserWithDefaultConstructor() {
        User user = new User();

        assertNull(user.getEmail());
        assertNull(user.getPassword());
    }

    @Test
    void shouldUpdateEmailUsingSetter() {
        User user = new User("old@paxtech.com", "password");

        user.setEmail("new@paxtech.com");

        assertEquals("new@paxtech.com", user.getEmail());
    }

    @Test
    void shouldUpdatePasswordUsingSetter() {
        User user = new User("test@paxtech.com", "oldPassword");

        user.setPassword("newPassword456");

        assertEquals("newPassword456", user.getPassword());
    }

    @Test
    void shouldAllowDifferentUsersToHaveDifferentEmails() {
        User userA = new User("a@paxtech.com", "passA");
        User userB = new User("b@paxtech.com", "passB");

        assertNotEquals(userA.getEmail(), userB.getEmail());
        assertNotEquals(userA.getPassword(), userB.getPassword());
    }
}
