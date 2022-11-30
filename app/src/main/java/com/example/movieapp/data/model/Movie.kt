package com.example.movieapp.data.model

data class Movie(
    var id : Int = 0,
    val Title: String,
    val Year: String,
    val imdbID: String,
    val Type: String,
    val Poster: String,
    var fav : Int = 0
)