package com.example.openmoviedatabase;

public class MovieDataDefault {
    public String movieName;
    public String movieYear;
    public String movieImdb;
    public String movieImg;

    public MovieDataDefault(String movieName, String movieYear, String movieImdb, String movieImg) {
        this.movieName = movieName;
        this.movieYear = movieYear;
        this.movieImdb = movieImdb;
        this.movieImg = movieImg;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieYear() {
        return movieYear;
    }

    public void setMovieYear(String movieYear) {
        this.movieYear = movieYear;
    }

    public String getMovieImdb() {
        return movieImdb;
    }

    public void setMovieImdb(String movieImdb) {
        this.movieImdb = movieImdb;
    }

    public String getMovieImg() {
        return movieImg;
    }

    public void setMovieImg(String movieImg) {
        this.movieImg = movieImg;
    }
}
