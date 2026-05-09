package com.paxtech.utime.platform.profiles.domain.model.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompanyNameTest {

    @Test
    void shouldHoldValue() {
        CompanyName name = new CompanyName("Pax Salon");

        assertEquals("Pax Salon", name.getValue());
    }

    @Test
    void shouldAllowDefaultConstructor() {
        CompanyName name = new CompanyName();

        assertNull(name.getValue());
    }

    @Test
    void shouldBeEqualForSameValue() {
        CompanyName a = new CompanyName("Studio");
        CompanyName b = new CompanyName("Studio");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
