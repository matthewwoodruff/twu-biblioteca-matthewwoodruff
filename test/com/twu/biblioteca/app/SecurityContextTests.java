package com.twu.biblioteca.app;

import com.twu.biblioteca.app.SecurityContext;
import com.twu.biblioteca.domain.Customer;
import com.twu.biblioteca.exceptions.CustomerRequiredException;
import com.twu.biblioteca.exceptions.InvalidCredentialsException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Matt on 27/02/15.
 */
public class SecurityContextTests {

    private SecurityContext securityContext;
    @Mock
    private Customer customer;

    private Set<Customer> customers;

    @Before
    public void setup() throws InvalidCredentialsException {
        initMocks(this);

        when(customer.getLibraryNumber()).thenReturn("123-4567");

        customers = new HashSet<>();
        customers.add(customer);
        securityContext = new SecurityContext(customers);
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
        loginCustomer();
        verify(customer, times(1)).verifyPassword("Password1");
        assertThat(securityContext.isCustomerLoggedIn(), is(true));
    }

    @Test
    public void testCanLoginAndLogout() throws IOException, InvalidCredentialsException, CustomerRequiredException {
        loginCustomer();
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
        doThrow(new InvalidCredentialsException()).when(customer).verifyPassword("Password2");
        securityContext.login("123-4567", "Password2");
    }

    @Test
    public void testGetLoggedInCustomer() throws InvalidCredentialsException, IOException, CustomerRequiredException {
        loginCustomer();
        assertThat(securityContext.getLoggedInCustomer(), is(customer));
    }

    @Test(expected = CustomerRequiredException.class)
    public void testGetLoggedInCustomerWhenNoCustomerLoggedInThrowsAnException() throws CustomerRequiredException {
        securityContext.getLoggedInCustomer();
    }

    private void loginCustomer() throws InvalidCredentialsException, IOException {
        securityContext.login("123-4567", "Password1");
    }

}
