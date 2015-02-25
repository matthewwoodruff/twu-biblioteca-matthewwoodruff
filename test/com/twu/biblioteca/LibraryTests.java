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

    private Library<Book> library;

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
        library = new Library<Book>(books);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLibraryConstructorDisallowsNullItems() {
        new Library<Book>(null);
    }

    @Test
    public void testGetListOfItems() {
        assertThat(library.getItems(), is(Arrays.asList(greatExpectations, pickwickPapers)));
    }

    @Test
    public void testCheckoutItemThatExistsAndIsAvailable() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        library.checkoutItem(greatExpectations, customer);
        assertThat(greatExpectations.isCheckedOut(), is(true));
    }

    @Test(expected = LibraryItemNotAvailableException.class)
    public void testCheckoutItemThatExistsButIsUnavailable() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        library.checkoutItem(greatExpectations, customer);
        library.checkoutItem(greatExpectations, customer);
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testCheckoutItemThatDoesNotExist() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        library.checkoutItem(bleakHouse, customer);
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testCheckoutNullItem() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        library.checkoutItem(null, customer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckoutItemWithNullCustomerThrowsAnException() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        library.checkoutItem(greatExpectations, null);
    }

    @Test
    public void testCheckoutItemByTitle() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        library.checkoutItemByTitle("Great Expectations", customer);
        assertThat(greatExpectations.isCheckedOut(), is(true));
        assertThat(library.getItems(), is(Arrays.asList(pickwickPapers)));
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testCheckOutItemByTitleThatDoesntExist() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        library.checkoutItemByTitle("Hard Times", customer);
    }

    @Test(expected = LibraryItemNotAvailableException.class)
    public void testCheckOutItemByTitleThatIsntAvailable() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        library.checkoutItem(greatExpectations, customer);
        library.checkoutItemByTitle("Great Expectations", customer);
    }

    @Test
    public void testCheckedOutItemDoesNotShowInItemList() throws LibraryItemNotFoundException, LibraryItemNotAvailableException {
        library.checkoutItem(greatExpectations, customer);
        assertThat(library.getItems(), is(Arrays.asList(pickwickPapers)));
    }

    @Test
    public void testReturnItemThatExistsAndIsCheckedOut() throws LibraryItemNotFoundException, LibraryItemNotAvailableException, LibraryItemNotCheckedOutException {
        library.checkoutItem(greatExpectations, customer);
        library.returnItem(greatExpectations);
        assertThat(greatExpectations.isAvailable(), is(true));
    }

    @Test(expected = LibraryItemNotCheckedOutException.class)
    public void testReturnItemThatExistsButIsntCheckedOut() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        library.returnItem(greatExpectations);
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testReturnItemThatDoesntExist() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        library.returnItem(bleakHouse);
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testReturnNullItem() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        library.returnItem(null);
    }

    @Test
    public void testReturnItemByTitle() throws LibraryItemNotFoundException, LibraryItemNotAvailableException, LibraryItemNotCheckedOutException {
        library.checkoutItem(greatExpectations, customer);
        library.returnItemByTitle("Great Expectations");
        assertThat(greatExpectations.isAvailable(), is(true));
    }

    @Test(expected = LibraryItemNotFoundException.class)
    public void testReturnItemByTitleThatDoesntExist() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        library.returnItemByTitle("Hard Times");
    }

    @Test(expected = LibraryItemNotCheckedOutException.class)
    public void testReturnItemByTitleThatHasntBeenCheckedOut() throws LibraryItemNotFoundException, LibraryItemNotCheckedOutException {
        library.returnItemByTitle("Great Expectations");
    }

    @Test
    public void testReturnedItemShowsUpInItemList() throws LibraryItemNotFoundException, LibraryItemNotAvailableException, LibraryItemNotCheckedOutException {
        library.checkoutItem(greatExpectations, customer);
        library.returnItem(greatExpectations);
        assertThat(library.getItems(), is(Arrays.asList(greatExpectations,pickwickPapers)));
    }

    @Test
    public void testFindItemByTitle() {
        assertThat(library.findItemByTitle("Great Expectations"), is(greatExpectations));
    }

    @Test
    public void testFindItemByNullTitle() {
        assertThat(library.findItemByTitle(null), is(nullValue()));
    }

    @Test
    public void testFindItemByTitleThatDoesntExist() {
        assertThat(library.findItemByTitle("Hard Times"), is(nullValue()));
    }

}
