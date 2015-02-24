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

    private Library library;

    private Book greatExpectations;
    private Book pickwickPapers;
    private Book bleakHouse;

    private Set<Book> books;

    private Movie pulpFiction;
    private Movie reserviorDogs;
    private Movie killBill;

    private Set<Movie> movies;

    @Before
    public void setup() {
        greatExpectations = new Book(1, "Great Expectations", "Charles Dickens", "1860");
        pickwickPapers = new Book(2, "The Pickwick Papers", "Charles Dickens", "1837");
        bleakHouse = new Book(3, "Bleak House", "Charles Dickens", "1853");
        books = new HashSet<Book>();
        books.add(greatExpectations);
        books.add(pickwickPapers);

        pulpFiction = Movie.createRatedMovie(1, "Pulp Fiction", "Quentin Tarantino", "1994", 9);
        reserviorDogs = Movie.createRatedMovie(1, "Reservior Dogs", "Quentin Tarantino", "1992", 8);
        killBill = Movie.createUnratedMovie(1, "Kill Bill", "Quentin Tarantino", "2003");
        movies = new HashSet<Movie>();
        movies.add(pulpFiction);
        movies.add(reserviorDogs);

        library = new Library(books, movies);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLibraryConstructorDisallowsNullBooks() {
        new Library(null, movies);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLibraryConstructorDisallowsNullMovies() {
        new Library(books, null);
    }

    @Test
    public void testGetListOfBooks() {
        assertThat(library.getBooks(), is(Arrays.asList(greatExpectations, pickwickPapers)));
    }

    @Test
    public void testGetListOfMovies() {
        assertThat(library.getMovies(), is(Arrays.asList(pulpFiction, reserviorDogs)));
    }

    @Test
    public void testCheckoutBookThatExistsAndIsAvailable() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        library.checkout(greatExpectations);
    }

    @Test
    public void testCheckoutMovieThatExistsAndIsAvailable() throws LibraryItemNotAvailableException, LibraryItemNotFoundException {
        library.checkoutItem(pulpFiction);
    }

    @Test(expected = LibraryItemNotAvailableException.class)
    public void testCheckoutBookThatExistsButIsUnavailable() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        library.checkout(greatExpectations);
        library.checkout(greatExpectations);
    }

    @Test(expected = LibraryItemNotAvailableException.class)
    public void testCheckoutMovieThatExistsButIsUnavailable() throws LibraryItemNotAvailableException, LibraryItemNotFoundException {
        library.checkoutItem(pulpFiction);
        library.checkoutItem(pulpFiction);
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testCheckoutBookThatDoesNotExist() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        library.checkout(bleakHouse);
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testCheckoutMovieThatDoesNotExist() throws LibraryItemNotAvailableException, LibraryItemNotFoundException {
        library.checkoutItem(killBill);
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testCheckoutNullBook() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        library.checkout((Book) null);
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testCheckoutNullMovie() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        library.checkout(null);
    }

    @Test
    public void testCheckOutBookByTitle() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        library.checkoutBook("Great Expectations");
        assertThat(library.getBooks(), is(Arrays.asList(pickwickPapers)));
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testCheckOutBookByTitleThatDoesntExist() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        library.checkoutBook("Hard Times");
    }

    @Test(expected = LibraryItemNotAvailableException.class)
    public void testCheckOutBookByTitleThatIsntAvailable() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        library.checkout(greatExpectations);
        library.checkoutBook("Great Expectations");
    }

    @Test
    public void testCheckedOutBookDoesNotShowInBookList() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        library.checkout(greatExpectations);
        assertThat(library.getBooks(), is(Arrays.asList(pickwickPapers)));
    }

    @Test
    public void testReturnBookThatExistsAndIsCheckedOut() throws LibraryItemNotFoundException, LibraryItemNotAvailableException, LibraryItemNotCheckedOutException {
        library.checkout(greatExpectations);
        library.returnItem(greatExpectations);
    }

    @Test(expected = LibraryItemNotCheckedOutException.class)
    public void testReturnBookThatExistsButIsntCheckedOut() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        library.returnItem(greatExpectations);
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testReturnBookThatDoesntExist() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        library.returnItem(bleakHouse);
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testReturnNullBook() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        library.returnItem((Book) null);
    }

    @Test
    public void testReturnBookByTitle() throws LibraryItemNotFoundException, LibraryItemNotAvailableException, LibraryItemNotCheckedOutException {
        library.checkout(greatExpectations);
        library.returnBook("Great Expectations");
        assertThat(library.getBooks(), is(Arrays.asList(greatExpectations, pickwickPapers)));
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testReturnBookByTitleThatDoesntExist() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        library.returnBook("Hard Times");
    }

    @Test(expected = LibraryItemNotCheckedOutException.class)
    public void testReturnBookByTitleThatHasntBeenCheckedOut() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        library.returnBook("Great Expectations");
    }

    @Test
    public void testReturnedBookShowsUpInBookList() throws LibraryItemNotFoundException, LibraryItemNotAvailableException, LibraryItemNotCheckedOutException {
        library.checkout(greatExpectations);
        library.returnItem(greatExpectations);
        assertThat(library.getBooks(), is(Arrays.asList(greatExpectations,pickwickPapers)));
    }

    @Test
    public void testFindBookByTitle() {
        assertThat(library.findBookByTitle("Great Expectations"), is(greatExpectations));
    }

    @Test
    public void testFindBookByNullTitle() {
        assertThat(library.findBookByTitle(null), is(nullValue()));
    }

    @Test
    public void testFindBookByTitleThatDoesntExist() {
        assertThat(library.findBookByTitle("Hard Times"), is(nullValue()));
    }

}
