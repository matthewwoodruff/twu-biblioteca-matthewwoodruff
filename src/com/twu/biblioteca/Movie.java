package com.twu.biblioteca;

import com.twu.biblioteca.exceptions.LibraryItemNotAvailableException;
import com.twu.biblioteca.exceptions.LibraryItemNotCheckedOutException;

/**
 * Created by Matt on 24/02/15.
 */
public class Movie {

    private final int id;
    private final String title;
    private final String year;
    private final String director;
    private final Integer rating;
    private boolean checkedOut = false;

    public Movie(final int id, final String title, final String year,
                 final String director, final Integer rating) {
        if(title == null || title.isEmpty()) throw new IllegalArgumentException("title cannot be null or empty");
        if(year == null || year.isEmpty()) throw new IllegalArgumentException("year cannot be null or empty");
        if(director == null || director.isEmpty()) throw new IllegalArgumentException("director cannot be null or empty");
        if(rating != null && (rating < 1 || rating > 10)) throw new IllegalArgumentException("rating must be between 1 and 10");
        this.id = id;
        this.title = title;
        this.year = year;
        this.director = director;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getDirector() {
        return director;
    }

    public Integer getRating() {
        return rating;
    }

    public static Movie createUnratedMovie(int id, String title, String year, String director) {
        return new Movie(id, title, year, director, null);
    }

    public static Movie createRatedMovie(int id, String title, String year, String director, int rating) {
        return new Movie(id, title, year, director, rating);
    }

    public boolean isCheckedOut() {
        return checkedOut;
    }

    public boolean isAvailable() {
        return !checkedOut;
    }

    public void checkOut() throws LibraryItemNotAvailableException {
        if(isCheckedOut()) throw new LibraryItemNotAvailableException();
        checkedOut = true;
    }

    public void checkIn() throws LibraryItemNotCheckedOutException {
        if(isAvailable()) throw new LibraryItemNotCheckedOutException();
        checkedOut = false;
    }
}

