package com.twu.biblioteca;

import com.twu.biblioteca.exceptions.UserPasswordIncorrectException;

/**
 * Created by Matt on 24/02/15.
 */
public class Customer {

    private final String firstName;
    private final String lastName;
    private final String emailAddress;
    private final String password;
    private final String libraryNumber;

    public Customer(final String firstName, final String lastName, final String emailAddress,
                    final String password, final String libraryNumber) {
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

    public void verifyPassword(String enteredPassword) throws UserPasswordIncorrectException {
        if(!password.equals(enteredPassword))
            throw new UserPasswordIncorrectException();
    }

    public String getLibraryNumber() {
        return libraryNumber;
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

        return true;
    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + emailAddress.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + libraryNumber.hashCode();
        return result;
    }

}
