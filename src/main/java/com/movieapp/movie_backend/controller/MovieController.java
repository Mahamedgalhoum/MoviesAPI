package com.movieapp.movie_backend.controller;
import com.movieapp.movie_backend.dto.MovieDto;
import com.movieapp.movie_backend.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;
@RestController
@RequestMapping(value = "/movies", produces = "application/json;charset=UTF-8")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieDto>> searchMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String actor,
            @RequestParam(required = false) String director) {

        List<MovieDto> movies = movieService.searchMovie(title, actor, director);
        return movies.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(movies);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<MovieDto> addMovie(@RequestBody MovieDto movieDto) {
        MovieDto savedMovie = movieService.addMovie(movieDto);
        return ResponseEntity.ok(savedMovie);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> removeMovie(@PathVariable Long id) {
        movieService.removeMovie(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        List<MovieDto> movies = movieService.getAllMovies();
        return movies.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(movies);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/favorite/{imdbId}")
    public ResponseEntity<MovieDto> addToFavorites(@PathVariable String imdbId) {
        MovieDto movie = movieService.addMovieToFavorites(imdbId);
        return ResponseEntity.ok(movie);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/favorites")
    public ResponseEntity<Set<MovieDto>> getFavoriteMovies() {
        Set<MovieDto> favoriteMovies = movieService.getFavoriteMovies();
        return favoriteMovies.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(favoriteMovies);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{movieId}/rate")
    public ResponseEntity<MovieDto> rateMovie(@PathVariable Long movieId, @RequestParam int rating) {
        MovieDto updatedMovie = movieService.rateMovie(movieId, rating);
        return ResponseEntity.ok(updatedMovie);
    }
}
