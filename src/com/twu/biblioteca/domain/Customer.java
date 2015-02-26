package com.twu.biblioteca.domain;

import com.twu.biblioteca.exceptions.InvalidCredentialsException;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Matt on 24/02/15.
 */
public final class Customer {

    private final String firstName;
    private final String lastName;
    private final String emailAddress;
    private final String password;
    private final String libraryNumber;
    private final String phoneNumber;

    public Customer(final String firstName, final String lastName, final String emailAddress,
                    final String password, final String libraryNumber, final String phoneNumber) {
        if(firstName == null || firstName.isEmpty()) throw new IllegalArgumentException("firstName cannot be null or empty");
        if(lastName == null || lastName.isEmpty()) throw new IllegalArgumentException("lastName cannot be null or empty");
        if(emailAddress == null || emailAddress.isEmpty()) throw new IllegalArgumentException("emailAddress cannot be null or empty");
        if(password == null || password.isEmpty()) throw new IllegalArgumentException("password cannot be null or empty");
        if(libraryNumber == null || libraryNumber.isEmpty()) throw new IllegalArgumentException("libraryNumber cannot be null or empty");
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.password = password;
        this.libraryNumber = libraryNumber;
        this.phoneNumber = phoneNumber;
    }

    public Customer(final String firstName, final String lastName, final String emailAddress,
                    final String password, final String libraryNumber) {
        this(firstName, lastName, emailAddress, password, libraryNumber, null);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void verifyPassword(String enteredPassword) throws InvalidCredentialsException {
        if(!password.equals(enteredPassword))
            throw new InvalidCredentialsException();
    }

    public String getLibraryNumber() {
        return libraryNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (!emailAddress.equals(customer.emailAddress)) return false;
        if (!firstName.equals(customer.firstName)) return false;
        if (!lastName.equals(customer.lastName)) return false;
        if (!libraryNumber.equals(customer.libraryNumber)) return false;
        if (!password.equals(customer.password)) return false;
        if (phoneNumber != null ? !phoneNumber.equals(customer.phoneNumber) : customer.phoneNumber != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + emailAddress.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + libraryNumber.hashCode();
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        return result;
    }

    public static Set<Customer> getCustomers() {
        final Set<Customer> customers = new HashSet<>();
        customers.add(new Customer("Charles", "Dickens", "charles@example.com", "Password1", "123-4567"));
        customers.add(new Customer("Quentin", "Tarantino", "quentin@example.com", "Password2", "234-5678"));
        return customers;
    }

    public String viewDetails() {
        return "Name: " + firstName + " " + lastName +
                "\nEmail Address: " + emailAddress +
                (phoneNumber == null ? "" : "\nPhone: " + phoneNumber);
    }

}
