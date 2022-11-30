package com.example.movieapp.data.datasource

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.movieapp.data.model.FavMovies
import com.example.movieapp.data.model.MovieDetail
import com.example.movieapp.data.model.MoviesResponse
import com.example.movieapp.retrofit.MovieDao
import com.example.movieapp.room.FavMoviesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class DataSource(var movieDao: MovieDao, var favMoviesDao: FavMoviesDao) {
    suspend fun searchMovie(s: String): MoviesResponse = withContext(Dispatchers.IO) {
        try {
            movieDao.searchMovie(s)
        } catch (e: Exception) {
            MoviesResponse(listOf(), null, false)
        }
    }

    suspend fun moreMovies(s: String, page: Int): MoviesResponse = withContext(Dispatchers.IO) {
        try {
            movieDao.moreMovies(s, page)
        } catch (e: Exception) {
            MoviesResponse(listOf(), null, false)
        }
    }

    suspend fun getMovieDetail(imdbID: String): MovieDetail = withContext(Dispatchers.IO) {
        try {
            movieDao.getMovieDetail(imdbID)

        } catch (e: Exception) {
            MovieDetail("", "", "", "", "", "", "", "", "", "", "", "", "", false)
        }
    }

    suspend fun getFavMovies(): Flow<List<FavMovies>> = withContext(Dispatchers.IO) {
        favMoviesDao.getFavMovies()
    }

    suspend fun addMovie(favMovies: FavMovies) = withContext(Dispatchers.IO) {
        favMoviesDao.addMovie(favMovies)
    }

    suspend fun delete(imdbID: String) = withContext(Dispatchers.IO) {
        favMoviesDao.delete(imdbID)
    }
}