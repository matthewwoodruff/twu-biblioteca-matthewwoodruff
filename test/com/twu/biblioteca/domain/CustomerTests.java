package com.twu.biblioteca.domain;

import com.twu.biblioteca.domain.Customer;
import com.twu.biblioteca.exceptions.InvalidCredentialsException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Matt on 24/02/15.
 */
public class CustomerTests {

    private Customer customer;

    @Before
    public void setup() {
        customer = new Customer("Charles", "Dickens", "charles@example.com", "Password1", "123-4567");
    }

    @Test
    public void testUserHasAFirstName() {
        assertThat(customer.getFirstName(), is("Charles"));
    }

    @Test
    public void testUserHasLastName() {
        assertThat(customer.getLastName(), is("Dickens"));
    }

    @Test
    public void testUserHasEmailAddress() {
        assertThat(customer.getEmailAddress(), is("charles@example.com"));
    }

    @Test
    public void testUserCanBeVerifiedWithPassword() throws InvalidCredentialsException {
        customer.verifyPassword("Password1");
    }

    @Test(expected = InvalidCredentialsException.class)
    public void testVerificationWithWrongPasswordThrowsException() throws InvalidCredentialsException {
        customer.verifyPassword("wrong password");
    }

    @Test
    public void testUserHasLibraryNumber() {
        assertThat(customer.getLibraryNumber(), is("123-4567"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUserCannotBeConstructedWithNullFirstName() {
        new Customer(null, "Dickens", "charles@example.com", "Password1", "123-4567");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUserCannotBeConstructedWithEmptyFirstName() {
        new Customer("", "Dickens", "charles@example.com", "Password1", "123-4567");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUserCannotBeConstructedWithNullLastName() {
        new Customer("Charles", null, "charles@example.com", "Password1", "123-4567");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUserCannotBeConstructedWithEmptyLastName() {
        new Customer("Charles", "", "charles@example.com", "Password1", "123-4567");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUserCannotBeConstructedWithNullEmailAddress() {
        new Customer("Charles", "Dickens", null, "Password1", "123-4567");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUserCannotBeConstructedWithEmptyEmailAddress() {
        new Customer("Charles", "Dickens", "", "Password1", "123-4567");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUserCannotBeConstructedWithNullPassword() {
        new Customer("Charles", "Dickens", "charles@example.com", null, "123-4567");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUserCannotBeConstructedWithEmptyPassword() {
        new Customer("Charles", "Dickens", "charles@example.com", "", "123-4567");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUserCannotBeConstructedWithNullLibraryNumber() {
        new Customer("Charles", "Dickens", "charles@example.com", "Password1", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUserCannotBeConstructedWithEmptyLibraryNumber() {
        new Customer("Charles", "Dickens", "charles@example.com", "Password1", "");
    }

    @Test
    public void testUserIsEqualToAnotherUser() {
        assertThat(customer, is(new Customer("Charles", "Dickens", "charles@example.com", "Password1", "123-4567")));
    }

}
