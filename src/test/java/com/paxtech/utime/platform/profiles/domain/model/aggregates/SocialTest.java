package com.paxtech.utime.platform.profiles.domain.model.aggregates;

import com.paxtech.utime.platform.profiles.domain.model.commands.CreateSocialCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.UpdateSocialCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SocialTest {

    @Test
    void shouldCreateWithValidValues() {
        Social social = new Social(new CreateSocialCommand("https://instagram.com/pax", "instagram"));

        assertEquals("https://instagram.com/pax", social.getSocialUrl());
        assertEquals("instagram", social.getSocialIcon());
    }

    @Test
    void shouldRejectBlankUrl() {
        assertThrows(IllegalArgumentException.class,
                () -> new Social(new CreateSocialCommand("  ", "instagram")));
    }

    @Test
    void shouldRejectBlankIcon() {
        assertThrows(IllegalArgumentException.class,
                () -> new Social(new CreateSocialCommand("https://x.com/pax", "")));
    }

    @Test
    void shouldRejectUrlLongerThan100() {
        String longUrl = "https://" + "a".repeat(120);

        assertThrows(IllegalArgumentException.class,
                () -> new Social(new CreateSocialCommand(longUrl, "instagram")));
    }

    @Test
    void shouldRejectIconLongerThan32() {
        String longIcon = "i".repeat(40);

        assertThrows(IllegalArgumentException.class,
                () -> new Social(new CreateSocialCommand("https://x.com/p", longIcon)));
    }

    @Test
    void shouldUpdateUrlAndIcon() {
        Social social = new Social(new CreateSocialCommand("https://old.com", "old"));

        social.update(new UpdateSocialCommand(1L, "https://new.com", "new"));

        assertEquals("https://new.com", social.getSocialUrl());
        assertEquals("new", social.getSocialIcon());
    }
}
