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
        if(title == null || title.isEmpty()) throw new IllegalArgumentException("title cannot be null or empty");
        if(year == null || year.isEmpty()) throw new IllegalArgumentException("year cannot be null or empty");
        if(director == null || director.isEmpty()) throw new IllegalArgumentException("director cannot be null or empty");
        if(rating != null && (rating < 1 || rating > 10)) throw new IllegalArgumentException("rating must be between 1 and 10");
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

    public Integer getRating() {
        return rating;
    }

    public static Movie createUnratedMovie(String title, String year, String director) {
        return new Movie(title, year, director, null);
    }

    public static Movie createRatedMovie(String title, String year, String director, int rating) {
        return new Movie(title, year, director, rating);
    }
}

