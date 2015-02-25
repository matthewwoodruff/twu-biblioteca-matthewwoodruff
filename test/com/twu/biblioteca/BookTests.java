package com.twu.biblioteca;

import com.twu.biblioteca.exceptions.LibraryItemNotAvailableException;
import com.twu.biblioteca.exceptions.LibraryItemNotCheckedOutException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Matt on 23/02/15.
 */
public class BookTests {

    private Book book;

    @Before
    public void setup() {
        book = new Book("Great Expectations", "Charles Dickens", "1860");
    }

    @Test
    public void testBookHasTitle() {
        assertThat(book.getTitle(), is("Great Expectations"));
    }

    @Test
    public void testBookHasAuthor() {
        assertThat(book.getAuthor(), is("Charles Dickens"));
    }

    @Test
    public void testBookHasYear() {
        assertThat(book.getYear(), is("1860"));
    }

    @Test
    public void testBookHasCheckedOutIndication() {
        assertThat(book.isCheckedOut(), is(false));
    }

    @Test
    public void testBookHasAvailableIndication() {
        assertThat(book.isAvailable(), is(true));
    }

    @Test
    public void testBookCanBeCheckedOut() throws LibraryItemNotAvailableException {
        book.checkOut();
        assertThat(book.isCheckedOut(), is(true));
    }

    @Test(expected = LibraryItemNotAvailableException.class)
    public void testCheckingOutAnUnavailableBookThrowsAnException() throws LibraryItemNotAvailableException {
        book.checkOut();
        book.checkOut();
    }

    @Test
    public void testBookCanBeCheckedIn() throws LibraryItemNotAvailableException, LibraryItemNotCheckedOutException {
        book.checkOut();
        book.checkIn();
        assertThat(book.isCheckedOut(), is(false));
    }

    @Test(expected = LibraryItemNotCheckedOutException.class)
    public void testCheckingInABookThatHasntBeenCheckedOutThrowsAnException() throws LibraryItemNotCheckedOutException {
        book.checkIn();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBookConstructorDisallowsNullTitle() {
        new Book(null, "Charles Dickens", "1860");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBookConstructorDisallowsEmptyTitle() {
        new Book("", "Charles Dickens", "1860");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBookConstructorDisallowsNullAuthor() {
        new Book("Great Expectations", null, "1860");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBookConstructorDisallowsEmptyAuthor() {
        new Book("Great Expectations", "", "1860");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBookConstructorDisallowsNullYear() {
        new Book("Great Expectations", "Charles Dickens", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBookConstructorDisallowsEmptyYear() {
        new Book("Great Expectations", "Charles Dickens", "");
    }

    @Test
    public void testBookEquality() {
        final Book book = new Book("Great Expectations", "Charles Dickens", "1860");
        assertThat(book, is(book));
        assertThat(book.hashCode(), is(book.hashCode()));
    }

}
