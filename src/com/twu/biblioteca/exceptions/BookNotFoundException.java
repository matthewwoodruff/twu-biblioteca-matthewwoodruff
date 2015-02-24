package com.twu.biblioteca.exceptions;

import com.twu.biblioteca.Book;

/**
 * Created by Matt on 23/02/15.
 */
public class BookNotFoundException extends Exception {

    public BookNotFoundException(Book book) {
        super(book + " not found in library");
    }

}