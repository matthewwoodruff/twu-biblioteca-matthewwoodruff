package com.twu.biblioteca.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Matt on 23/02/15.
 */
public class Book extends LibraryItem<Book> {

    private final String author;
    private static final String CSV_HEADERS = "Title, Author, Year";

    public Book(String title, String author, String year) {
        super(title, year);
        if(author == null || author.isEmpty()) throw new IllegalArgumentException("author cannot be null or empty");
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Book book = (Book) o;

        if (!author.equals(book.author)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + author.hashCode();
        return result;
    }

    @Override
    public String getCSVRepresentation() {
        return getTitle() + ", " + author + ", " + getYear();
    }

    @Override
    public String getCSVHeaders() {
        return CSV_HEADERS;
    }

    public static Set<Book> getBooks() {
        final Set<Book> books = new HashSet<>();
        books.add(new Book("Great Expectations", "Charles Dickens", "1860"));
        books.add(new Book("The Pickwick Papers", "Charles Dickens", "1837"));
        books.add(new Book("Bleak House", "Charles Dickens", "1853"));
        return books;
    }

}
