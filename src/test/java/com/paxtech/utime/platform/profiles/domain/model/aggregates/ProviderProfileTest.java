package com.paxtech.utime.platform.profiles.domain.model.aggregates;

import com.paxtech.utime.platform.profiles.domain.model.commands.CreateProviderProfileCommand;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ProviderProfileTest {

    private CreateProviderProfileCommand baseCommand() {
        return new CreateProviderProfileCommand(
                "https://cdn/profile.png",
                "https://cdn/cover.png",
                "Lima, Peru",
                10L,
                "Best salon in town",
                LocalTime.of(9, 0),
                LocalTime.of(20, 0)
        );
    }

    @Test
    void shouldCreateFromCommand() {
        ProviderProfile profile = new ProviderProfile(baseCommand());

        assertEquals("https://cdn/profile.png", profile.getProfileImageUrl());
        assertEquals("https://cdn/cover.png", profile.getCoverImageUrl());
        assertEquals("Lima, Peru", profile.getLocation());
        assertEquals(10L, profile.getProviderId());
        assertEquals("Best salon in town", profile.getDescription());
        assertEquals(LocalTime.of(9, 0), profile.getOpenTime());
        assertEquals(LocalTime.of(20, 0), profile.getCloseTime());
    }

    @Test
    void updateInformationShouldOverrideOnlyProvidedFields() {
        ProviderProfile profile = new ProviderProfile(baseCommand());

        profile.updateInformation(null, null, "Cusco, Peru", null, null, LocalTime.of(22, 0));

        assertEquals("https://cdn/profile.png", profile.getProfileImageUrl());
        assertEquals("Cusco, Peru", profile.getLocation());
        assertEquals(LocalTime.of(22, 0), profile.getCloseTime());
        assertEquals(LocalTime.of(9, 0), profile.getOpenTime());
    }

    @Test
    void shouldUpdateIndividualFields() {
        ProviderProfile profile = new ProviderProfile(baseCommand());

        profile.updateDescription("New description");
        profile.updateOpenTime(LocalTime.of(8, 30));
        profile.updateCloseTime(LocalTime.of(21, 30));
        profile.updateProfileImageUrl("https://cdn/new.png");
        profile.updateCoverImageUrl("https://cdn/newcover.png");

        assertEquals("New description", profile.getDescription());
        assertEquals(LocalTime.of(8, 30), profile.getOpenTime());
        assertEquals(LocalTime.of(21, 30), profile.getCloseTime());
        assertEquals("https://cdn/new.png", profile.getProfileImageUrl());
        assertEquals("https://cdn/newcover.png", profile.getCoverImageUrl());
    }

    @Test
    void shouldHaveEmptyDefaultConstructor() {
        ProviderProfile profile = new ProviderProfile();

        assertNull(profile.getDescription());
        assertNull(profile.getOpenTime());
    }
}
