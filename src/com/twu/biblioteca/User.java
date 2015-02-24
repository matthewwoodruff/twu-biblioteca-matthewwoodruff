package com.twu.biblioteca;

/**
 * Created by Matt on 24/02/15.
 */
public class User {

    private final String firstName;
    private final String lastName;

    public User(final String firstName, final String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

}
