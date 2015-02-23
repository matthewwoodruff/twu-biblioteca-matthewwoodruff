package com.twu.biblioteca;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Matt on 23/02/15.
 */
public class LibraryTests {

    @Test
    public void testGetListOfBooks() {
        List<Book> books = Arrays.asList(new Book("Great Expectations", "Charles Dickens", "1860"));
        Library library = new Library(books);
        assertThat(library.getBooks(), is(books));
    }

}
