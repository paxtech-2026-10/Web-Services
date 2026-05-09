package com.paxtech.utime.platform.profiles.domain.model.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FullNameTest {

    @Test
    void shouldComposeFirstAndLastName() {
        FullName fullName = new FullName("Gael", "Ramirez");

        assertEquals("Gael", fullName.getFirstName());
        assertEquals("Ramirez", fullName.getLastName());
        assertEquals("Gael Ramirez", fullName.getFullName());
    }

    @Test
    void shouldReturnEmptyWhenBothNamesAreNull() {
        FullName fullName = new FullName();

        assertEquals("", fullName.getFullName());
    }

    @Test
    void shouldReturnLastNameWhenFirstNameIsNull() {
        FullName fullName = new FullName(null, "Ramirez");

        assertEquals("Ramirez", fullName.getFullName());
    }

    @Test
    void shouldReturnFirstNameWhenLastNameIsNull() {
        FullName fullName = new FullName("Gael", null);

        assertEquals("Gael", fullName.getFullName());
    }

    @Test
    void shouldBeEqualForSameValues() {
        FullName a = new FullName("Gael", "Ramirez");
        FullName b = new FullName("Gael", "Ramirez");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
