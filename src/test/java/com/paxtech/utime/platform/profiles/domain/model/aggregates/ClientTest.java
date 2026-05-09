package com.paxtech.utime.platform.profiles.domain.model.aggregates;

import com.paxtech.utime.platform.iam.domain.model.aggregates.User;
import com.paxtech.utime.platform.profiles.domain.model.commands.CreateClientCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    private User newUser() {
        return new User("client@paxtech.com", "password123");
    }

    @Test
    void shouldCreateClientFromCommand() {
        CreateClientCommand command = new CreateClientCommand("Gael", "Ramirez", 1L);
        User user = newUser();

        Client client = new Client(command, user);

        assertEquals("Gael", client.getFirstName());
        assertEquals("Ramirez", client.getLastName());
        assertEquals("Gael Ramirez", client.getFullName());
        assertSame(user, client.getUser());
    }

    @Test
    void shouldUpdateFullName() {
        Client client = new Client(new CreateClientCommand("Gael", "Ramirez", 1L), newUser());

        client.updateFullName("Alejandro", "Perez");

        assertEquals("Alejandro", client.getFirstName());
        assertEquals("Perez", client.getLastName());
        assertEquals("Alejandro Perez", client.getFullName());
    }

    @Test
    void shouldRejectBlankFirstNameOnUpdate() {
        Client client = new Client(new CreateClientCommand("Gael", "Ramirez", 1L), newUser());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> client.updateFullName("   ", "Perez"));
        assertTrue(ex.getMessage().contains("First name"));
    }

    @Test
    void shouldRejectNullLastNameOnUpdate() {
        Client client = new Client(new CreateClientCommand("Gael", "Ramirez", 1L), newUser());

        assertThrows(IllegalArgumentException.class,
                () -> client.updateFullName("Alejandro", null));
    }

    @Test
    void shouldUpdateProfileImageUrl() {
        Client client = new Client(new CreateClientCommand("Gael", "Ramirez", 1L), newUser());

        client.updateProfileImageUrl("https://cdn.paxtech.com/avatar.png");

        assertEquals("https://cdn.paxtech.com/avatar.png", client.getProfileImageUrl());
    }
}
