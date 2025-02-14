package com.movieapp.movie_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OmdbService {

    private final RestTemplate restTemplate;

    @Value("${omdb.api.url}")
    private String omdbApiUrl;

    @Value("${omdb.api.key}")
    private String omdbApiKey;

    public OmdbService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     *  البحث عن الأفلام في OMDB API باستخدام العنوان فقط.
     */
    public List<Map<String, Object>> searchMovies(String title, String actor, String director) {
        if (title == null || title.isEmpty()) {
            return Collections.emptyList();
        }

        String url = String.format("%s?apikey=%s&s=%s", omdbApiUrl, omdbApiKey, title);
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        return response != null && response.containsKey("Search")
                ? (List<Map<String, Object>>) response.get("Search")
                : Collections.emptyList();
    }

    /**
     *  جلب تفاصيل فيلم معين من OMDB API باستخدام IMDb ID.
     */
    public Map<String, Object> getMovieDetails(String imdbID) {
        if (imdbID == null || imdbID.isEmpty()) {
            return Collections.emptyMap();
        }

        String url = String.format("%s?apikey=%s&i=%s", omdbApiUrl, omdbApiKey, imdbID);
        return restTemplate.getForObject(url, Map.class);
    }

    /**
     *  البحث عن الأفلام مع التصفية حسب الممثل أو المخرج.
     */
    public List<Map<String, Object>> searchMoviesWithFilters(String title, String actor, String director) {
        List<Map<String, Object>> movies = searchMovies(title, actor, director);
        List<Map<String, Object>> filteredMovies = new ArrayList<>();

        for (Map<String, Object> movie : movies) {
            String imdbID = (String) movie.get("imdbID");
            Map<String, Object> movieDetails = getMovieDetails(imdbID);

            if (movieDetails != null) {
                String movieActors = movieDetails.getOrDefault("Actors", "").toString();
                String movieDirector = movieDetails.getOrDefault("Director", "").toString();

                boolean matchesActor = (actor == null || movieActors.contains(actor));
                boolean matchesDirector = (director == null || movieDirector.contains(director));

                if (matchesActor && matchesDirector) {
                    filteredMovies.add(movieDetails);
                }
            }
        }
        return filteredMovies;
    }
}
