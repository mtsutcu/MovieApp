package com.example.movieapp.retrofit

import com.example.movieapp.data.model.MovieDetail
import com.example.movieapp.data.model.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieDao {

    @GET("?apikey=b3de1be0")
    suspend fun searchMovie(@Query("s") s:String): MoviesResponse

    @GET("?apikey=b3de1be0")
    suspend fun moreMovies( @Query("s") s:String,@Query("page") page : Int): MoviesResponse

    @GET("?apikey=b3de1be0")
    suspend fun getMovieDetail(@Query("i") imdbID : String) : MovieDetail
}
