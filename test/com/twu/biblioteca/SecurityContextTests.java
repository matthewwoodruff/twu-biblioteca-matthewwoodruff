package com.twu.biblioteca;

import com.twu.biblioteca.domain.Customer;
import com.twu.biblioteca.exceptions.CustomerRequiredException;
import com.twu.biblioteca.exceptions.InvalidCredentialsException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Matt on 27/02/15.
 */
public class SecurityContextTests {

    private SecurityContext securityContext;
    private Customer customer;

    @Before
    public void setup() throws InvalidCredentialsException {
        securityContext = new SecurityContext(Customer.getCustomers());
        customer = mock(Customer.class);
        when(customer.getLibraryNumber()).thenReturn("123-4567");
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
        assertThat(securityContext.isCustomerLoggedIn(), is(true));
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

    @Test
    public void testGetLoggedInCustomer() throws InvalidCredentialsException, IOException, CustomerRequiredException {
        setCustomer();
        assertThat(securityContext.getLoggedInCustomer(), is(customer));
    }

    @Test(expected = CustomerRequiredException.class)
    public void testGetLoggedInCustomerWhenNoCustomerLoggedInThrowsAnException() throws CustomerRequiredException {
        securityContext.getLoggedInCustomer();
    }

    private void setCustomer() throws InvalidCredentialsException, IOException {
        securityContext.setCustomer(customer, "Password1");
    }

}
