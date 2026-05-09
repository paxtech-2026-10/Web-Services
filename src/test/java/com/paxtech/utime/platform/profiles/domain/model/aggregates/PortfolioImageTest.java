package com.paxtech.utime.platform.profiles.domain.model.aggregates;

import com.paxtech.utime.platform.profiles.domain.model.commands.CreatePortfolioImageCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.UpdatePortfolioImageCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioImageTest {

    @Test
    void shouldCreateWithValidUrl() {
        PortfolioImage image = new PortfolioImage(new CreatePortfolioImageCommand("https://cdn/img.png"));

        assertEquals("https://cdn/img.png", image.getImageUrl());
    }

    @Test
    void shouldRejectNullUrl() {
        assertThrows(IllegalArgumentException.class,
                () -> new PortfolioImage(new CreatePortfolioImageCommand(null)));
    }

    @Test
    void shouldRejectBlankUrl() {
        assertThrows(IllegalArgumentException.class,
                () -> new PortfolioImage(new CreatePortfolioImageCommand("   ")));
    }

    @Test
    void shouldRejectUrlLongerThan200() {
        String longUrl = "https://cdn/" + "a".repeat(250);

        assertThrows(IllegalArgumentException.class,
                () -> new PortfolioImage(new CreatePortfolioImageCommand(longUrl)));
    }

    @Test
    void shouldUpdateUrl() {
        PortfolioImage image = new PortfolioImage(new CreatePortfolioImageCommand("https://cdn/old.png"));

        image.update(new UpdatePortfolioImageCommand(1L, "https://cdn/new.png"));

        assertEquals("https://cdn/new.png", image.getImageUrl());
    }

    @Test
    void updateShouldRejectInvalidUrl() {
        PortfolioImage image = new PortfolioImage(new CreatePortfolioImageCommand("https://cdn/old.png"));

        assertThrows(IllegalArgumentException.class,
                () -> image.update(new UpdatePortfolioImageCommand(1L, "")));
    }
}
