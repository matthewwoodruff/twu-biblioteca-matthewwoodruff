package com.twu.biblioteca;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Matt on 24/02/15.
 */
public final class Movie extends LibraryItem {

    private final String director;
    private final Integer rating;

    public Movie(final int id, final String title, final String year,
                 final String director, final Integer rating) {
        super(id, title, year);
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

    public static Movie createUnratedMovie(int id, String title, String year, String director) {
        return new Movie(id, title, year, director, null);
    }

    public static Movie createRatedMovie(int id, String title, String year, String director, int rating) {
        return new Movie(id, title, year, director, rating);
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

    protected static SortedSet<Movie> getDefaultMovies() {
        final SortedSet<Movie> movies = new TreeSet<Movie>();
        movies.add(Movie.createRatedMovie(1, "Pulp Fiction", "Quentin Tarantino", "1994", 9));
        movies.add(Movie.createRatedMovie(1, "Reservior Dogs", "Quentin Tarantino", "1992", 8));
        movies.add(Movie.createUnratedMovie(1, "Kill Bill", "Quentin Tarantino", "2003"));
        return movies;
    }

}

