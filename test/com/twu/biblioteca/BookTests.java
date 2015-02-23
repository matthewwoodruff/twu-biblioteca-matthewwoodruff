package com.twu.biblioteca;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

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
        assertEquals(book.getTitle(), "Great Expectations");
    }

    @Test
    public void testBookHasAuthor() {
        assertEquals(book.getAuthor(), "Charles Dickens");
    }

    @Test
    public void testBookHasYear() {
        assertEquals(book.getYear(), "1860");
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
        assertEquals(book, new Book("Great Expectations", "Charles Dickens", "1860"));
        assertEquals(book.hashCode(), new Book("Great Expectations", "Charles Dickens", "1860").hashCode());
    }
    
}
