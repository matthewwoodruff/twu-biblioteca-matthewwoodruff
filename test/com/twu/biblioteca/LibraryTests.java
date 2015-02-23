package com.twu.biblioteca;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Matt on 23/02/15.
 */
public class LibraryTests {

    private Library library;
    private Book greatExpectations;
    private Book pickwickPapers;
    private Book bleakHouse;

    @Before
    public void setup() {
        greatExpectations = new Book("Great Expectations", "Charles Dickens", "1860");
        pickwickPapers = new Book("The Pickwick Papers", "Charles Dickens", "1837");
        bleakHouse = new Book("Bleak House", "Charles Dickens", "1853");
        List<Book> books = new ArrayList();
        books.add(greatExpectations);
        books.add(pickwickPapers);
        library = new Library(books);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLibraryConstructorDisallowsNullBooks() {
        new Library(null);
    }

    @Test
    public void testGetListOfBooks() {
        assertThat(library.getBooks(), is(Arrays.asList(greatExpectations, pickwickPapers)));
    }

    @Test
    public void testCheckoutBookThatExistsAndIsAvailable() throws BookNotFoundException, BookNotAvailableException {
        library.checkoutBook(greatExpectations);
    }

    @Test(expected = BookNotAvailableException.class)
    public void testCheckoutBookThatExistsButIsUnavailable() throws BookNotFoundException, BookNotAvailableException {
        library.checkoutBook(greatExpectations);
        library.checkoutBook(greatExpectations);
    }

    @Test(expected = BookNotFoundException.class)
    public void testCheckoutBookThatDoesNotExist() throws BookNotFoundException, BookNotAvailableException {
        library.checkoutBook(bleakHouse);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckoutNullBook() throws BookNotFoundException, BookNotAvailableException {
        library.checkoutBook(null);
    }

    @Test
    public void testCheckedOutBookDoesNotShowInBookList() throws BookNotFoundException, BookNotAvailableException {
        library.checkoutBook(greatExpectations);
        assertThat(library.getBooks(), is(Arrays.asList(pickwickPapers)));
    }

    @Test
    public void testReturnBookThatExistsAndIsCheckedOut() throws BookNotFoundException, BookNotAvailableException, BookNotCheckedOutException {
        library.checkoutBook(greatExpectations);
        library.returnBook(greatExpectations);
    }

    @Test(expected = BookNotCheckedOutException.class)
    public void testReturnBookThatExistsButIsntCheckedOut() throws BookNotCheckedOutException, BookNotFoundException {
        library.returnBook(greatExpectations);
    }

    @Test(expected = BookNotFoundException.class)
    public void testReturnBookThatDoesntExist() throws BookNotCheckedOutException, BookNotFoundException {
        library.returnBook(bleakHouse);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReturnNullBook() throws BookNotCheckedOutException, BookNotFoundException {
        library.returnBook(null);
    }

    @Test
    public void testReturnedBookShowsUpInBookList() throws BookNotFoundException, BookNotAvailableException, BookNotCheckedOutException {
        library.checkoutBook(greatExpectations);
        library.returnBook(greatExpectations);
        assertThat(library.getBooks(), is(Arrays.asList(pickwickPapers, greatExpectations)));
    }


}
