package com.movieapp.movie_backend.dto;

public class MovieDto {
    private String imdbID;
    private String title;
    private String year;
    private String director;
    private String actors;
    private String poster;


    public MovieDto() {}

    public MovieDto(String imdbID, String title, String year, String director, String actors, String poster) {
        this.imdbID = imdbID;
        this.title = title;
        this.year = year;
        this.director = director;
        this.actors = actors;
        this.poster = poster;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
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

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    @Override
    public String toString() {
        return "MovieDto{" +
                "imdbID='" + imdbID + '\'' +
                ", title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", director='" + director + '\'' +
                ", actors='" + actors + '\'' +
                ", poster='" + poster + '\'' +
                '}';
    }
}