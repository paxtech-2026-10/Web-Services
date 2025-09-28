package com.paxtech.utime.platform.profiles.domain.model.valueobjects;

public record FullName(String firstName, String lastName) {

    public FullName() {
        this(null, null);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        if (firstName == null && lastName == null) return "";
        if (firstName == null) return lastName;
        if (lastName == null) return firstName;
        return firstName + " " + lastName;
    }
}
