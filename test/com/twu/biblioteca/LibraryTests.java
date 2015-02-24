package com.twu.biblioteca;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
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
        greatExpectations = new Book(1, "Great Expectations", "Charles Dickens", "1860");
        pickwickPapers = new Book(2, "The Pickwick Papers", "Charles Dickens", "1837");
        bleakHouse = new Book(3, "Bleak House", "Charles Dickens", "1853");
        Set<Book> books = new HashSet<Book>();
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

    @Test(expected = BookNotFoundException.class)
    public void testCheckoutNullBook() throws BookNotFoundException, BookNotAvailableException {
        library.checkoutBook((Book) null);
    }

    @Test
    public void testCheckOutBookByTitle() throws BookNotFoundException, BookNotAvailableException {
        library.checkoutBook("Great Expectations");
        assertThat(library.getBooks(), is(Arrays.asList(pickwickPapers)));
    }

    @Test(expected = BookNotFoundException.class)
    public void testCheckOutBookByTitleThatDoesntExist() throws BookNotFoundException, BookNotAvailableException {
        library.checkoutBook("Hard Times");
    }

    @Test(expected = BookNotAvailableException.class)
    public void testCheckOutBookByTitleThatIsntAvailable() throws BookNotFoundException, BookNotAvailableException {
        library.checkoutBook(greatExpectations);
        library.checkoutBook("Great Expectations");
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

    @Test(expected = BookNotFoundException.class)
    public void testReturnNullBook() throws BookNotCheckedOutException, BookNotFoundException {
        library.returnBook(null);
    }

    @Test
    public void testFindBookByTitle() {
        assertThat(library.findBookByTitle("Great Expectations"), is(greatExpectations));
    }

    @Test
    public void testFindBookByTitleThatDoesntExist() {
        assertThat(library.findBookByTitle("Hard Times"), is(nullValue()));
    }

    @Test
    public void testReturnedBookShowsUpInBookList() throws BookNotFoundException, BookNotAvailableException, BookNotCheckedOutException {
        library.checkoutBook(greatExpectations);
        library.returnBook(greatExpectations);
        assertThat(library.getBooks(), is(Arrays.asList(greatExpectations,pickwickPapers)));
    }


}
