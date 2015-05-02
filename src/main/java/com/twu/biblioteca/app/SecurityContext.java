package com.twu.biblioteca.app;

import com.twu.biblioteca.domain.Customer;
import com.twu.biblioteca.exceptions.CustomerRequiredException;
import com.twu.biblioteca.exceptions.InvalidCredentialsException;
import com.twu.biblioteca.helper.SecurityStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Matt on 27/02/15.
 */
public class SecurityContext implements SecurityStatus {

    private Customer customer;
    private Map<String, Customer> customers = new HashMap<>();

    public SecurityContext(Set<Customer> customers) {
        for(final Customer customer : customers)
            this.customers.put(customer.getLibraryNumber(), customer);
    }

    @Override
    public boolean isCustomerLoggedIn() {
        return customer != null;
    }

    void verifyCustomerIsLoggedIn() throws CustomerRequiredException {
        if(!isCustomerLoggedIn()) throw new CustomerRequiredException();
    }

    void setCustomer(Customer customer, String password) throws InvalidCredentialsException {
        if(customer == null) throw new InvalidCredentialsException();
        customer.verifyPassword(password);
        this.customer = customer;
    }

    void logout() throws CustomerRequiredException {
        verifyCustomerIsLoggedIn();
        customer = null;
    }

    void login(String libraryNumber, String password) throws InvalidCredentialsException, IOException {
        setCustomer(customers.get(libraryNumber), password);
    }

    public Customer getLoggedInCustomer() throws CustomerRequiredException {
        verifyCustomerIsLoggedIn();
        return customer;
    }
}
