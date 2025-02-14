package com.movieapp.movie_backend.repository;

import com.movieapp.movie_backend.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    //  البحث عن الأفلام بناءً على العنوان
    List<Movie> findByTitleContainingIgnoreCase(String title);

    // البحث عن الأفلام بناءً على المخرج
    List<Movie> findByDirectorContainingIgnoreCase(String director);

    //  البحث عن الأفلام بناءً على اسم الممثل
    List<Movie> findByActorsContainingIgnoreCase(String actor);

    //  البحث عن فيلم معين باستخدام IMDb ID
    Optional<Movie> findByImdbID(String imdbID);

    //  البحث عن الأفلام باستخدام (العنوان أو المخرج أو الممثل)
    List<Movie> findByTitleContainingIgnoreCaseOrDirectorContainingIgnoreCaseOrActorsContainingIgnoreCase(
            String title, String director, String actor
    );
}
