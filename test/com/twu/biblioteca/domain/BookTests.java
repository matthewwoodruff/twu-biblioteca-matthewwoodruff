package com.twu.biblioteca.domain;

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
    public void testBookHasAuthor() {
        assertThat(book.getAuthor(), is("Charles Dickens"));
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

    @Test
    public void testBookHasCSVRepresentation() {
        assertThat(book.getCSVRepresentation(), is("Great Expectations, Charles Dickens, 1860"));
    }

    @Test
    public void testBookHasCSVHeaders() {
        assertThat(book.getCSVHeaders(), is("Title, Author, Year"));
    }

//    @Test
//    public void testDisplayItemsInCSVFormat() {
//        final List<String> csvLines = Arrays.asList(
//                "List Books",
//                "Checkout Book: Great Expectations",
//                "Return Book: Great Expectations",
//                "Quit");
//        assertThat(library.toCSV(), is(StringUtils.join(csvLines, "\n")));
//    }

}
