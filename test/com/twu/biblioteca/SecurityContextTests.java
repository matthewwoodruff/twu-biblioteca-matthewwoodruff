package com.twu.biblioteca;

import com.twu.biblioteca.domain.Customer;
import com.twu.biblioteca.exceptions.CustomerRequiredException;
import com.twu.biblioteca.exceptions.InvalidCredentialsException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Scanner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Matt on 27/02/15.
 */
public class SecurityContextTests {

    private SecurityContext securityContext;
    private Customer customer;

    @Before
    public void setup() {
        securityContext = new SecurityContext(Customer.getCustomers());
        customer = Customer.getCustomers().iterator().next();
    }

    /*
     * Customer login/logout
     */

    @Test
    public void testCanDetermineIfCustomerLoggedIn() {
        assertThat(securityContext.isCustomerLoggedIn(), is(false));
    }

    @Test
    public void testCanLogIn() throws InvalidCredentialsException, IOException {
        setCustomer();
        assertThat(securityContext.isCustomerLoggedIn(), is(true));
    }

    @Test
    public void testCanLoginAndLogout() throws IOException, InvalidCredentialsException, CustomerRequiredException {
        setCustomer();
        securityContext.logout();
        assertThat(securityContext.isCustomerLoggedIn(), is(false));
    }

    @Test(expected = CustomerRequiredException.class)
    public void testLogoutThrowsExceptionIfNotLoggedIn() throws IOException, InvalidCredentialsException, CustomerRequiredException {
        securityContext.logout();
    }

    @Test(expected = InvalidCredentialsException.class)
    public void testLoginWillThrowExceptionIfLibraryNumberNotKnown() throws InvalidCredentialsException, IOException {
        securityContext.login("234-5678", "Password1");
    }

    @Test(expected = InvalidCredentialsException.class)
    public void testLoginWithBadCredentials() throws InvalidCredentialsException, IOException {
        securityContext.login("123-4567", "Password2");
    }

    private void setCustomer() throws InvalidCredentialsException, IOException {
        securityContext.setCustomer(customer, "Password1");
    }

}
