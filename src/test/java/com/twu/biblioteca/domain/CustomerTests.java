package com.twu.biblioteca.domain;

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
        customer = new Customer("Charles", "Dickens", "charles@example.com", "Password1", "123-4567", "07712345678");
    }

    @Test
    public void testCustomerHasAFirstName() {
        assertThat(customer.getFirstName(), is("Charles"));
    }

    @Test
    public void testCustomerHasLastName() {
        assertThat(customer.getLastName(), is("Dickens"));
    }

    @Test
    public void testCustomerHasEmailAddress() {
        assertThat(customer.getEmailAddress(), is("charles@example.com"));
    }

    @Test
    public void testCustomerHasPhoneNumber() {
        assertThat(customer.getPhoneNumber(), is("07712345678"));
    }

    @Test
    public void testCustomerCanBeVerifiedWithPassword() throws InvalidCredentialsException {
        customer.verifyPassword("Password1");
    }

    @Test(expected = InvalidCredentialsException.class)
    public void testVerificationWithWrongPasswordThrowsException() throws InvalidCredentialsException {
        customer.verifyPassword("wrong password");
    }

    @Test
    public void testCustomerHasLibraryNumber() {
        assertThat(customer.getLibraryNumber(), is("123-4567"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCustomerCannotBeConstructedWithNullFirstName() {
        new Customer(null, "Dickens", "charles@example.com", "Password1", "123-4567");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCustomerCannotBeConstructedWithEmptyFirstName() {
        new Customer("", "Dickens", "charles@example.com", "Password1", "123-4567");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCustomerCannotBeConstructedWithNullLastName() {
        new Customer("Charles", null, "charles@example.com", "Password1", "123-4567");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCustomerCannotBeConstructedWithEmptyLastName() {
        new Customer("Charles", "", "charles@example.com", "Password1", "123-4567");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCustomerCannotBeConstructedWithNullEmailAddress() {
        new Customer("Charles", "Dickens", null, "Password1", "123-4567");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCustomerCannotBeConstructedWithEmptyEmailAddress() {
        new Customer("Charles", "Dickens", "", "Password1", "123-4567");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCustomerCannotBeConstructedWithNullPassword() {
        new Customer("Charles", "Dickens", "charles@example.com", null, "123-4567");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCustomerCannotBeConstructedWithEmptyPassword() {
        new Customer("Charles", "Dickens", "charles@example.com", "", "123-4567");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCustomerCannotBeConstructedWithNullLibraryNumber() {
        new Customer("Charles", "Dickens", "charles@example.com", "Password1", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCustomerCannotBeConstructedWithEmptyLibraryNumber() {
        new Customer("Charles", "Dickens", "charles@example.com", "Password1", "");
    }

    @Test
    public void testCustomerIsEqualToAnotherCustomer() {
        assertThat(customer, is(new Customer("Charles", "Dickens", "charles@example.com", "Password1", "123-4567", "07712345678")));
    }

    @Test
    public void testViewCustomerDetails() {
        assertThat(customer.viewDetails(), is("Name: Charles Dickens\nEmail Address: charles@example.com\nPhone: 07712345678"));
    }

    @Test
    public void testViewCustomerDetailsForCustomerWithNoPhone() {
        customer = new Customer("Charles", "Dickens", "charles@example.com", "Password1", "123-4567");
        assertThat(customer.viewDetails(), is("Name: Charles Dickens\nEmail Address: charles@example.com"));
    }

}
