package com.twu.biblioteca;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Matt on 23/02/15.
 */
public final class Book extends LibraryItem<Book> {

    private final String author;

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
    public String toString() {
        return "Book{" +
                "author='" + author + '\'' +
                "} " + super.toString();
    }

    @Override
    public String getCSVRepresentation() {
        return getTitle() + ", " + author + ", " + getYear();
    }

    public static String getCSVHeaders() {
        return "Title, Author, Year";
    }

    protected static Set<Book> getDefaultBooks() {
        final Set<Book> books = new HashSet<Book>();
        books.add(new Book("Great Expectations", "Charles Dickens", "1860"));
        books.add(new Book("The Pickwick Papers", "Charles Dickens", "1837"));
        books.add(new Book("Bleak House", "Charles Dickens", "1853"));
        return books;
    }

}
