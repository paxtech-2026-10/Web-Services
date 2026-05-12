package com.paxtech.utime.platform.profiles.domain.model.aggregates;

import com.paxtech.utime.platform.iam.domain.model.aggregates.User;
import com.paxtech.utime.platform.profiles.domain.model.commands.CreateProviderCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProviderTest {

    private User newUser() {
        return new User("provider@paxtech.com", "password123");
    }

    @Test
    void shouldCreateProviderFromCommand() {
        Provider provider = new Provider(new CreateProviderCommand("Pax Salon", 1L), newUser());

        assertEquals("Pax Salon", provider.getCompanyName());
        assertNotNull(provider.getUser());
    }

    @Test
    void shouldUpdateCompanyName() {
        Provider provider = new Provider(new CreateProviderCommand("Old Name", 1L), newUser());

        provider.updateCompanyName("New Studio");

        assertEquals("New Studio", provider.getCompanyName());
    }

    @Test
    void shouldRejectBlankCompanyNameOnUpdate() {
        Provider provider = new Provider(new CreateProviderCommand("Old Name", 1L), newUser());

        assertThrows(IllegalArgumentException.class, () -> provider.updateCompanyName("  "));
    }

    @Test
    void shouldRejectNullCompanyNameOnUpdate() {
        Provider provider = new Provider(new CreateProviderCommand("Old Name", 1L), newUser());

        assertThrows(IllegalArgumentException.class, () -> provider.updateCompanyName(null));
    }
}
