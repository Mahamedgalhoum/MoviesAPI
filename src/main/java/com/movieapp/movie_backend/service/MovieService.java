package com.movieapp.movie_backend.service;

import com.movieapp.movie_backend.dto.MovieDto;
import com.movieapp.movie_backend.exception.MovieNotFoundException;
import com.movieapp.movie_backend.exception.UserNotFoundException;
import com.movieapp.movie_backend.model.Movie;
import com.movieapp.movie_backend.model.User;
import com.movieapp.movie_backend.repository.MovieRepository;
import com.movieapp.movie_backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final OmdbService omdbService;

    public MovieService(MovieRepository movieRepository, UserRepository userRepository, OmdbService omdbService) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.omdbService = omdbService;
    }

    public List<MovieDto> searchMovie(String title, String actor, String director) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return omdbService.searchMovies(title, actor, director)
                    .stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } else {
            return searchMovieInDatabase(title, actor, director);
        }
    }

    private List<MovieDto> searchMovieInDatabase(String title, String actor, String director) {
        return movieRepository.findByTitleContainingIgnoreCase(title).stream()
                .filter(movie -> (actor == null || movie.getActors().toLowerCase().contains(actor.toLowerCase())) &&
                        (director == null || movie.getDirector().toLowerCase().contains(director.toLowerCase())))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public MovieDto addMovie(MovieDto movieDto) {
        Movie movie = movieRepository.findByImdbID(movieDto.getImdbID())
                .orElseGet(() -> movieRepository.save(convertToEntity(movieDto)));
        return convertToDto(movie);
    }

    public MovieDto addMovieToFavorites(String imdbID) {
        User user = getAuthenticatedUser();
        Movie movie = movieRepository.findByImdbID(imdbID)
                .orElseGet(() -> fetchAndSaveMovieFromOMDB(imdbID));

        user.getFavoriteMovies().add(movie);
        userRepository.save(user);
        return convertToDto(movie);
    }

    private Movie fetchAndSaveMovieFromOMDB(String imdbID) {
        Map<String, Object> response = omdbService.getMovieDetails(imdbID);

        if (response == null || response.isEmpty()) {
            throw new MovieNotFoundException("Movie not found in OMDB API");
        }

        Movie movie = convertToEntity(new MovieDto(
                imdbID,
                response.getOrDefault("Title", "").toString(),
                response.getOrDefault("Year", "").toString(),
                response.getOrDefault("Director", "").toString(),
                response.getOrDefault("Actors", "").toString(),
                response.getOrDefault("Poster", "").toString()
        ));

        return movieRepository.save(movie);
    }

    public Set<MovieDto> getFavoriteMovies() {
        return getAuthenticatedUser().getFavoriteMovies()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toSet());
    }

    public MovieDto rateMovie(Long movieId, int rating) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found"));
        movie.addRating(rating);
        return convertToDto(movieRepository.save(movie));
    }

    public List<MovieDto> getAllMovies() {
        return movieRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void removeMovie(Long movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new MovieNotFoundException("Movie not found");
        }
        movieRepository.deleteById(movieId);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private MovieDto convertToDto(Movie movie) {
        return new MovieDto(
                movie.getImdbID(),
                movie.getTitle(),
                movie.getYear(),
                movie.getDirector(),
                movie.getActors(),
                movie.getPoster()
        );
    }

    private Movie convertToEntity(MovieDto dto) {
        Movie movie = new Movie();
        movie.setImdbID(dto.getImdbID());
        movie.setTitle(dto.getTitle());
        movie.setYear(dto.getYear());
        movie.setDirector(dto.getDirector());
        movie.setActors(dto.getActors());
        movie.setPoster(dto.getPoster());
        return movie;
    }

    private MovieDto convertToDto(Map<String, Object> movieMap) {
        return new MovieDto(
                (String) movieMap.get("imdbID"),
                (String) movieMap.get("Title"),
                (String) movieMap.get("Year"),
                (String) movieMap.get("Director"),
                (String) movieMap.get("Actors"),
                (String) movieMap.get("Poster")
        );
    }

}
