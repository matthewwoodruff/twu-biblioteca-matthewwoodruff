package com.twu.biblioteca;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Matt on 23/02/15.
 */
public final class Book extends LibraryItem {

    private final String author;

    public Book(int id, String title, String author, String year) {
        super(id, title, year);
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
                '}';
    }

    protected static SortedSet<Book> getDefaultBooks() {
        final SortedSet<Book> books = new TreeSet<Book>();
        books.add(new Book(1, "Great Expectations", "Charles Dickens", "1860"));
        books.add(new Book(2, "The Pickwick Papers", "Charles Dickens", "1837"));
        books.add(new Book(3, "Bleak House", "Charles Dickens", "1853"));
        return books;
    }

}
