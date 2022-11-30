package com.example.movieapp.data.repo

import com.example.movieapp.data.datasource.DataSource
import com.example.movieapp.data.model.FavMovies
import com.example.movieapp.data.model.MovieDetail
import com.example.movieapp.data.model.MoviesResponse
import kotlinx.coroutines.flow.Flow

class MovieRepository(var dataSource: DataSource) {
    suspend fun searchMovie(s: String): MoviesResponse = dataSource.searchMovie(s)
    suspend fun moreMovies(s: String,page :Int ): MoviesResponse = dataSource.moreMovies(s,page)
    suspend fun getMovieDetail(imdbID : String) : MovieDetail = dataSource.getMovieDetail(imdbID)
    suspend fun getFavMovies() : Flow<List<FavMovies>> = dataSource.getFavMovies()
    suspend fun addMovie(favMovies: FavMovies) =dataSource.addMovie(favMovies)
    suspend fun delete(imdbID: String) = dataSource.delete(imdbID)
}