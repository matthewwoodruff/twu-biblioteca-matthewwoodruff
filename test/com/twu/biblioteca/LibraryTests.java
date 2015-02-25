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
    private Library<Movie> movieLibrary;

    private Book greatExpectations;
    private Book pickwickPapers;
    private Book bleakHouse;

    private Set<Book> books;

    private Movie pulpFiction;
    private Movie reservoirDogs;
    private Movie killBill;

    private Set<Movie> movies;

    @Before
    public void setup() {
        greatExpectations = new Book("Great Expectations", "Charles Dickens", "1860");
        pickwickPapers = new Book("The Pickwick Papers", "Charles Dickens", "1837");
        bleakHouse = new Book("Bleak House", "Charles Dickens", "1853");
        books = new HashSet<Book>();
        books.add(greatExpectations);
        books.add(pickwickPapers);
        bookLibrary = new Library<Book>(books);

        pulpFiction = Movie.createRatedMovie("Pulp Fiction", "Quentin Tarantino", "1994", 9);
        reservoirDogs = Movie.createRatedMovie("Reservior Dogs", "Quentin Tarantino", "1992", 8);
        killBill = Movie.createUnratedMovie("Kill Bill", "Quentin Tarantino", "2003");
        movies = new HashSet<Movie>();
        movies.add(pulpFiction);
        movies.add(reservoirDogs);
        movieLibrary = new Library<Movie>(movies);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLibraryConstructorDisallowsNullBooks() {
        new Library<Book>(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLibraryConstructorDisallowsNullMovies() {
        new Library<Movie>(null);
    }

    @Test
    public void testGetListOfBooks() {
        assertThat(bookLibrary.getItems(), is(Arrays.asList(greatExpectations, pickwickPapers)));
    }

    @Test
    public void testGetListOfMovies() {
        assertThat(movieLibrary.getItems(), is(Arrays.asList(pulpFiction, reservoirDogs)));
    }

    @Test
    public void testCheckoutBookThatExistsAndIsAvailable() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        bookLibrary.checkoutItem(greatExpectations);
        assertThat(greatExpectations.isCheckedOut(), is(true));
    }

    @Test
    public void testCheckoutMovieThatExistsAndIsAvailable() throws LibraryItemNotAvailableException, LibraryItemNotFoundException {
        movieLibrary.checkoutItem(pulpFiction);
        assertThat(pulpFiction.isCheckedOut(), is(true));
    }

    @Test(expected = LibraryItemNotAvailableException.class)
    public void testCheckoutBookThatExistsButIsUnavailable() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        bookLibrary.checkoutItem(greatExpectations);
        bookLibrary.checkoutItem(greatExpectations);
    }

    @Test(expected = LibraryItemNotAvailableException.class)
    public void testCheckoutMovieThatExistsButIsUnavailable() throws LibraryItemNotAvailableException, LibraryItemNotFoundException {
        movieLibrary.checkoutItem(pulpFiction);
        movieLibrary.checkoutItem(pulpFiction);
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testCheckoutBookThatDoesNotExist() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        bookLibrary.checkoutItem(bleakHouse);
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testCheckoutMovieThatDoesNotExist() throws LibraryItemNotAvailableException, LibraryItemNotFoundException {
        movieLibrary.checkoutItem(killBill);
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testCheckoutNullItem() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        movieLibrary.checkoutItem(null);
    }

    @Test
    public void testCheckoutBookByTitle() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        bookLibrary.checkoutItemByTitle("Great Expectations");
        assertThat(bookLibrary.getItems(), is(Arrays.asList(pickwickPapers)));
    }

    @Test
    public void testCheckoutMovieByTitle() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        movieLibrary.checkoutItemByTitle("Pulp Fiction");
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testCheckOutBookByTitleThatDoesntExist() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        bookLibrary.checkoutItemByTitle("Hard Times");
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testCheckOutMovieByTitleThatDoesntExist() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        movieLibrary.checkoutItemByTitle("Death Proof");
    }

    @Test(expected = LibraryItemNotAvailableException.class)
    public void testCheckOutBookByTitleThatIsntAvailable() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        bookLibrary.checkoutItem(greatExpectations);
        bookLibrary.checkoutItemByTitle("Great Expectations");
    }

    @Test(expected = LibraryItemNotAvailableException.class)
    public void testCheckOutMovieByTitleThatIsntAvailable() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        movieLibrary.checkoutItem(pulpFiction);
        movieLibrary.checkoutItemByTitle("Pulp Fiction");
    }

    @Test
    public void testCheckedOutBookDoesNotShowInBookList() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        bookLibrary.checkoutItem(greatExpectations);
        assertThat(bookLibrary.getItems(), is(Arrays.asList(pickwickPapers)));
    }

    @Test
    public void testCheckedOutMovieDoesNotShowInMovieList() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        movieLibrary.checkoutItem(pulpFiction);
        assertThat(movieLibrary.getItems(), is(Arrays.asList(reservoirDogs)));
    }

    @Test
    public void testReturnBookThatExistsAndIsCheckedOut() throws LibraryItemNotFoundException, LibraryItemNotAvailableException, LibraryItemNotCheckedOutException {
        bookLibrary.checkoutItem(greatExpectations);
        bookLibrary.returnItem(greatExpectations);
    }

    @Test
    public void testReturnMovieThatExistsAndIsCheckedOut() throws LibraryItemNotFoundException, LibraryItemNotAvailableException, LibraryItemNotCheckedOutException {
        movieLibrary.checkoutItem(pulpFiction);
        movieLibrary.returnItem(pulpFiction);
        assertThat(pulpFiction.isAvailable(), is(true));
    }

    @Test(expected = LibraryItemNotCheckedOutException.class)
    public void testReturnBookThatExistsButIsntCheckedOut() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        bookLibrary.returnItem(greatExpectations);
    }

    @Test(expected = LibraryItemNotCheckedOutException.class)
    public void testReturnMovieThatExistsButIsntCheckedOut() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        movieLibrary.returnItem(pulpFiction);
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testReturnBookThatDoesntExist() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        bookLibrary.returnItem(bleakHouse);
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testReturnMovieThatDoesntExist() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        movieLibrary.returnItem(killBill);
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testReturnNullItem() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        movieLibrary.returnItem(null);
    }

    @Test
    public void testReturnBookByTitle() throws LibraryItemNotFoundException, LibraryItemNotAvailableException, LibraryItemNotCheckedOutException {
        bookLibrary.checkoutItem(greatExpectations);
        bookLibrary.returnItemByTitle("Great Expectations");
        assertThat(greatExpectations.isAvailable(), is(true));
    }

    @Test
    public void testReturnMovieByTitle() throws LibraryItemNotFoundException, LibraryItemNotAvailableException, LibraryItemNotCheckedOutException {
        movieLibrary.checkoutItem(pulpFiction);
        movieLibrary.returnItemByTitle("Pulp Fiction");
        assertThat(pulpFiction.isAvailable(), is(true));
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testReturnBookByTitleThatDoesntExist() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        bookLibrary.returnItemByTitle("Hard Times");
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testReturnMovieByTitleThatDoesntExist() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        movieLibrary.returnItemByTitle("Kill Bill");
    }

    @Test(expected = LibraryItemNotCheckedOutException.class)
    public void testReturnBookByTitleThatHasntBeenCheckedOut() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        bookLibrary.returnItemByTitle("Great Expectations");
    }

    @Test(expected = LibraryItemNotCheckedOutException.class)
    public void testReturnMovieByTitleThatHasntBeenCheckedOut() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        movieLibrary.returnItemByTitle("Pulp Fiction");
    }

    @Test
    public void testReturnedBookShowsUpInBookList() throws LibraryItemNotFoundException, LibraryItemNotAvailableException, LibraryItemNotCheckedOutException {
        bookLibrary.checkoutItem(greatExpectations);
        bookLibrary.returnItem(greatExpectations);
        assertThat(bookLibrary.getItems(), is(Arrays.asList(greatExpectations,pickwickPapers)));
    }

    @Test
    public void testReturnedMovieShowsUpInMovieList() throws LibraryItemNotFoundException, LibraryItemNotAvailableException, LibraryItemNotCheckedOutException {
        movieLibrary.checkoutItem(pulpFiction);
        movieLibrary.returnItem(pulpFiction);
        assertThat(movieLibrary.getItems(), is(Arrays.asList(pulpFiction, reservoirDogs)));
    }

    @Test
    public void testFindBookByTitle() {
        assertThat(bookLibrary.findItemByTitle("Great Expectations"), is(greatExpectations));
    }

    @Test
    public void testFindMovieByTitle() {
        assertThat(movieLibrary.findItemByTitle("Pulp Fiction"), is(pulpFiction));
    }

    @Test
    public void testFindBookByNullTitle() {
        assertThat(bookLibrary.findItemByTitle(null), is(nullValue()));
    }

    @Test
    public void testFindMovieByNullTitle() {
        assertThat(movieLibrary.findItemByTitle(null), is(nullValue()));
    }

    @Test
    public void testFindBookByTitleThatDoesntExist() {
        assertThat(bookLibrary.findItemByTitle("Hard Times"), is(nullValue()));
    }

    @Test
    public void testFindMovieByTitleThatDoesntExist() {
        assertThat(movieLibrary.findItemByTitle("Death Proof"), is(nullValue()));
    }

}
