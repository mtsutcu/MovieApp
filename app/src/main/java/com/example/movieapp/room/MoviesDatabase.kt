package com.example.movieapp.room
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movieapp.data.model.FavMovies

@Database(entities = [FavMovies::class], version = 1)
abstract class MoviesDatabase : RoomDatabase(){
    abstract fun getFavMoviesDao() : FavMoviesDao
}