package com.twu.biblioteca;

/**
 * Created by Matt on 24/02/15.
 */
public class Movie {

    private final String title;
    private final String year;
    private final String director;
    private final Integer rating;

    public Movie(final String title, final String year,
                 final String director, final Integer rating) {
        this.title = title;
        this.year = year;
        this.director = director;
        this.rating = rating;
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

    public int getRating() {
        return rating;
    }
    
}

