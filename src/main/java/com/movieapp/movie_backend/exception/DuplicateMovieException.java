package com.movieapp.movie_backend.exception;

public class DuplicateMovieException extends RuntimeException {
    public DuplicateMovieException(String message) {
        super(message);
    }
}
