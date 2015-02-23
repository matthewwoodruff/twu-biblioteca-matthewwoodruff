package com.twu.biblioteca;

import java.util.List;

/**
 * Created by Matt on 23/02/15.
 */
public class Library {

    private final List<Book> books;

    public Library(List<Book> books) {
        this.books = books;
    }

    public List<Book> getBooks() {
        return books;
    }

}
