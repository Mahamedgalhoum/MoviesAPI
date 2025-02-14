package com.movieapp.movie_backend.model;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String year;
    private String imdbID;

    @Column(length = 500)
    private String poster;
    private String director;
    @Column(length = 1000)
    private String actors;
    private Double rating = 0.0;
    private int ratingCount = 0;
    private Double averageRating = 0.0;

    @ElementCollection
    private Set<Double> ratings = new HashSet<>();

    @ManyToMany(mappedBy = "favoriteMovies")
    private Set<User> likedByUsers = new HashSet<>();

    public Movie() {}

    public Movie(Long id, String title, String year, String imdbID, String poster, String director, String actors) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.imdbID = imdbID;
        this.poster = poster;
        this.director = director;
        this.actors = actors;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public Set<Double> getRatings() {
        return ratings;
    }

    public void addRating(Double rating) {
        ratings.add(rating);
        this.averageRating = ratings.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
    public void addRating(int newRating) {
        this.rating = ((this.rating * this.ratingCount) + newRating) / (this.ratingCount + 1);
        this.ratingCount++;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", imdbID='" + imdbID + '\'' +
                ", poster='" + poster + '\'' +
                ", director='" + director + '\'' +
                ", actors='" + actors + '\'' +
                ", averageRating=" + averageRating +
                '}';
    }
}
