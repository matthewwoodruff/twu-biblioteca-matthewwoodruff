package com.twu.biblioteca;

/**
 * Created by Matt on 23/02/15.
 */
public class Book {

    private final String title;
    private final String author;
    private final String year;

    public Book(String title, String author, String year) {
        if(title == null || title.isEmpty()) throw new IllegalArgumentException("title cannot be null or empty");
        if(author == null || author.isEmpty()) throw new IllegalArgumentException("author cannot be null or empty");
        if(year == null || year.isEmpty()) throw new IllegalArgumentException("year cannot be null or empty");
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getYear() {
        return year;
    }

}
