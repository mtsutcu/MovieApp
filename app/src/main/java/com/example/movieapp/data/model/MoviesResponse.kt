package com.example.movieapp.data.model

data class MoviesResponse(
    var Search: List<Movie> = listOf(),
    var totalResults: Int? = null,
    var Response : Boolean?
)