package com.twu.biblioteca.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Matt on 24/02/15.
 */
public class Movie extends LibraryItem<Movie> {

    private final String director;
    private final Integer rating;
    private static final String CSV_HEADERS = "Title, Director, Year, Rating";

    public Movie(final String title, final String year,
                 final String director, final Integer rating) {
        super(title, year);
        if(director == null || director.isEmpty()) throw new IllegalArgumentException("director cannot be null or empty");
        if(rating != null && (rating < 1 || rating > 10)) throw new IllegalArgumentException("rating must be between 1 and 10");
        this.director = director;
        this.rating = rating;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Movie movie = (Movie) o;

        if (!director.equals(movie.director)) return false;
        if (rating != null ? !rating.equals(movie.rating) : movie.rating != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + director.hashCode();
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "director='" + director + '\'' +
                ", rating=" + rating +
                "} " + super.toString();
    }

    @Override
    public String getCSVRepresentation() {
        return getTitle() + ", " + director + ", " + getYear() + ", " + (rating == null ? "Unrated" : rating);
    }

    @Override
    public String getCSVHeaders() {
        return CSV_HEADERS;
    }

    public static Set<Movie> getMovies() {
        final Set<Movie> movies = new HashSet<>();
        movies.add(Movie.createRatedMovie("Pulp Fiction", "1994", "Quentin Tarantino", 9));
        movies.add(Movie.createRatedMovie("Reservoir Dogs", "1992", "Quentin Tarantino", 8));
        movies.add(Movie.createUnratedMovie("Kill Bill", "2003", "Quentin Tarantino"));
        return movies;
    }

}

