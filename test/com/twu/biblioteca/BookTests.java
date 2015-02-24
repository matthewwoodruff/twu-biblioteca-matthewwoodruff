package com.twu.biblioteca;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Created by Matt on 23/02/15.
 */
public class BookTests {

    private Book book;

    @Before
    public void setup() {
        book = new Book(1, "Great Expectations", "Charles Dickens", "1860");
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
    public void testBookHasId() {
        assertThat(book.getId(), is(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBookConstructorDisallowsNullTitle() {
        new Book(1, null, "Charles Dickens", "1860");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBookConstructorDisallowsEmptyTitle() {
        new Book(1, "", "Charles Dickens", "1860");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBookConstructorDisallowsNullAuthor() {
        new Book(1, "Great Expectations", null, "1860");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBookConstructorDisallowsEmptyAuthor() {
        new Book(1, "Great Expectations", "", "1860");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBookConstructorDisallowsNullYear() {
        new Book(1, "Great Expectations", "Charles Dickens", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBookConstructorDisallowsEmptyYear() {
        new Book(1, "Great Expectations", "Charles Dickens", "");
    }

    @Test
    public void testBookEquality() {
        assertThat(book, is(new Book(1, "Great Expectations", "Charles Dickens", "1860")));
        assertThat(book.hashCode(), is(new Book(1, "Great Expectations", "Charles Dickens", "1860").hashCode()));
    }

}
