package com.twu.biblioteca;

import com.twu.biblioteca.exceptions.LibraryItemNotAvailableException;
import com.twu.biblioteca.exceptions.LibraryItemNotCheckedOutException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Matt on 25/02/15.
 */
public class LibraryItemTests {

    private LibraryItem item;

    @Before
    public void setup() {
        item = new Book("Great Expectations", "Charles Dickens", "1860");
    }

    @Test
    public void testItemHasTitle() {
        assertThat(item.getTitle(), is("Great Expectations"));
    }

    @Test
    public void testItemHasYear() {
        assertThat(item.getYear(), is("1860"));
    }

    @Test
    public void testItemHasCheckedOutIndication() {
        assertThat(item.isCheckedOut(), is(false));
    }

    @Test
    public void testItemHasAvailableIndication() {
        assertThat(item.isAvailable(), is(true));
    }

    @Test
    public void testItemCanBeCheckedOut() throws LibraryItemNotAvailableException {
        item.checkOut();
        assertThat(item.isCheckedOut(), is(true));
        assertThat(item.isAvailable(), is(false));
    }

    @Test(expected = LibraryItemNotAvailableException.class)
    public void testCheckingOutAnUnavailableItemThrowsAnException() throws LibraryItemNotAvailableException {
        item.checkOut();
        item.checkOut();
    }

    @Test
    public void testItemCanBeCheckedIn() throws LibraryItemNotAvailableException, LibraryItemNotCheckedOutException {
        item.checkOut();
        item.checkIn();
        assertThat(item.isCheckedOut(), is(false));
        assertThat(item.isAvailable(), is(true));
    }

    @Test(expected = LibraryItemNotCheckedOutException.class)
    public void testCheckingInAnItemThatHasntBeenCheckedOutThrowsAnException() throws LibraryItemNotCheckedOutException {
        item.checkIn();
    }

}
