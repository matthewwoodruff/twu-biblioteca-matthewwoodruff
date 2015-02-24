package com.twu.biblioteca;

import com.twu.biblioteca.exceptions.UserPasswordIncorrectException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Matt on 24/02/15.
 */
public class UserTests {

    private User user;

    @Before
    public void setup() {
        user = new User("Charles", "Dickens", "charles@example.com", "Password1", "123-4567");
    }

    @Test
    public void testUserHasAFirstName() {
        assertThat(user.getFirstName(), is("Charles"));
    }

    @Test
    public void testUserHasLastName() {
        assertThat(user.getLastName(), is("Dickens"));
    }

    @Test
    public void testUserHasEmailAddress() {
        assertThat(user.getEmailAddress(), is("charles@example.com"));
    }

    @Test
    public void testUserCanBeVerifiedWithPassword() throws UserPasswordIncorrectException {
        user.verifyPassword("Password1");
    }

    @Test(expected = UserPasswordIncorrectException.class)
    public void testVerificationWithWrongPasswordThrowsException() throws UserPasswordIncorrectException {
        user.verifyPassword("wrong password");
    }

    @Test
    public void testUserHasLibraryNumber() {
        assertThat(user.getLibraryNumber(), is("123-4567"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUserCannotBeConstructedWithNullFirstName() {
        new User(null, "Dickens", "charles@example.com", "Password1", "123-4567");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUserCannotBeConstructedWithEmptyFirstName() {
        new User("", "Dickens", "charles@example.com", "Password1", "123-4567");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUserCannotBeConstructedWithNullLastName() {
        new User("Charles", null, "charles@example.com", "Password1", "123-4567");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUserCannotBeConstructedWithEmptyLastName() {
        new User("Charles", "", "charles@example.com", "Password1", "123-4567");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUserCannotBeConstructedWithNullEmailAddress() {
        new User("Charles", "Dickens", null, "Password1", "123-4567");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUserCannotBeConstructedWithEmptyEmailAddress() {
        new User("Charles", "Dickens", "", "Password1", "123-4567");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUserCannotBeConstructedWithNullPassword() {
        new User("Charles", "Dickens", "charles@example.com", null, "123-4567");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUserCannotBeConstructedWithEmptyPassword() {
        new User("Charles", "Dickens", "charles@example.com", "", "123-4567");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUserCannotBeConstructedWithNullLibraryNumber() {
        new User("Charles", "Dickens", "charles@example.com", "Password1", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUserCannotBeConstructedWithEmptyLibraryNumber() {
        new User("Charles", "Dickens", "charles@example.com", "Password1", "");
    }

    @Test
    public void testUserIsEqualToAnotherUser() {
        assertThat(user, is(new User("Charles", "Dickens", "charles@example.com", "Password1", "123-4567")));
    }

}
