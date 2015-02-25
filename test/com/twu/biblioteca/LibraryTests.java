package com.twu.biblioteca;

import com.twu.biblioteca.exceptions.LibraryItemNotFoundException;
import com.twu.biblioteca.exceptions.LibraryItemNotAvailableException;
import com.twu.biblioteca.exceptions.LibraryItemNotCheckedOutException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Matt on 23/02/15.
 */
public class LibraryTests {

    private Library<Book> bookLibrary;

    private Book greatExpectations;
    private Book pickwickPapers;
    private Book bleakHouse;

    private Set<Book> books;

    private Customer customer;

    @Before
    public void setup() {
        customer = new Customer("Charles", "Dickens", "charles@example.com", "Password1", "123-4567");

        greatExpectations = new Book("Great Expectations", "Charles Dickens", "1860");
        pickwickPapers = new Book("The Pickwick Papers", "Charles Dickens", "1837");
        bleakHouse = new Book("Bleak House", "Charles Dickens", "1853");
        books = new HashSet<Book>();
        books.add(greatExpectations);
        books.add(pickwickPapers);
        bookLibrary = new Library<Book>(books);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLibraryConstructorDisallowsNullBooks() {
        new Library<Book>(null);
    }

    @Test
    public void testGetListOfBooks() {
        assertThat(bookLibrary.getItems(), is(Arrays.asList(greatExpectations, pickwickPapers)));
    }

    @Test
    public void testCheckoutBookThatExistsAndIsAvailable() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        bookLibrary.checkoutItem(greatExpectations, customer);
        assertThat(greatExpectations.isCheckedOut(), is(true));
    }

    @Test(expected = LibraryItemNotAvailableException.class)
    public void testCheckoutBookThatExistsButIsUnavailable() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        bookLibrary.checkoutItem(greatExpectations, customer);
        bookLibrary.checkoutItem(greatExpectations, customer);
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testCheckoutBookThatDoesNotExist() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        bookLibrary.checkoutItem(bleakHouse, customer);
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testCheckoutNullItem() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        bookLibrary.checkoutItem(null, customer);
    }

    @Test
    public void testCheckoutBookByTitle() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        bookLibrary.checkoutItemByTitle("Great Expectations", customer);
        assertThat(greatExpectations.isCheckedOut(), is(true));
        assertThat(bookLibrary.getItems(), is(Arrays.asList(pickwickPapers)));
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testCheckOutBookByTitleThatDoesntExist() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        bookLibrary.checkoutItemByTitle("Hard Times", customer);
    }

    @Test(expected = LibraryItemNotAvailableException.class)
    public void testCheckOutBookByTitleThatIsntAvailable() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        bookLibrary.checkoutItem(greatExpectations, customer);
        bookLibrary.checkoutItemByTitle("Great Expectations", customer);
    }

    @Test
    public void testCheckedOutBookDoesNotShowInBookList() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        bookLibrary.checkoutItem(greatExpectations, customer);
        assertThat(bookLibrary.getItems(), is(Arrays.asList(pickwickPapers)));
    }

    @Test
    public void testReturnBookThatExistsAndIsCheckedOut() throws LibraryItemNotFoundException, LibraryItemNotAvailableException, LibraryItemNotCheckedOutException {
        bookLibrary.checkoutItem(greatExpectations, customer);
        bookLibrary.returnItem(greatExpectations);
        assertThat(greatExpectations.isAvailable(), is(true));
    }

    @Test(expected = LibraryItemNotCheckedOutException.class)
    public void testReturnBookThatExistsButIsntCheckedOut() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        bookLibrary.returnItem(greatExpectations);
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testReturnBookThatDoesntExist() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        bookLibrary.returnItem(bleakHouse);
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testReturnNullItem() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        bookLibrary.returnItem(null);
    }

    @Test
    public void testReturnBookByTitle() throws LibraryItemNotFoundException, LibraryItemNotAvailableException, LibraryItemNotCheckedOutException {
        bookLibrary.checkoutItem(greatExpectations, customer);
        bookLibrary.returnItemByTitle("Great Expectations");
        assertThat(greatExpectations.isAvailable(), is(true));
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testReturnBookByTitleThatDoesntExist() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        bookLibrary.returnItemByTitle("Hard Times");
    }

    @Test(expected = LibraryItemNotCheckedOutException.class)
    public void testReturnBookByTitleThatHasntBeenCheckedOut() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        bookLibrary.returnItemByTitle("Great Expectations");
    }

    @Test
    public void testReturnedBookShowsUpInBookList() throws LibraryItemNotFoundException, LibraryItemNotAvailableException, LibraryItemNotCheckedOutException {
        bookLibrary.checkoutItem(greatExpectations, customer);
        bookLibrary.returnItem(greatExpectations);
        assertThat(bookLibrary.getItems(), is(Arrays.asList(greatExpectations,pickwickPapers)));
    }

    @Test
    public void testFindBookByTitle() {
        assertThat(bookLibrary.findItemByTitle("Great Expectations"), is(greatExpectations));
    }

    @Test
    public void testFindBookByNullTitle() {
        assertThat(bookLibrary.findItemByTitle(null), is(nullValue()));
    }

    @Test
    public void testFindBookByTitleThatDoesntExist() {
        assertThat(bookLibrary.findItemByTitle("Hard Times"), is(nullValue()));
    }

}
