package com.example.movieapp.retrofit

class ApiUtils {
    companion object{
        var BASE_URL = "https://www.omdbapi.com/?apikey=b3de1be0"
        fun getMovieDao():MovieDao{
            return RetrofitClient.getClient(BASE_URL).create(MovieDao::class.java)
        }
    }
}