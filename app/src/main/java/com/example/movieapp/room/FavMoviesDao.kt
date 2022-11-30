package com.example.movieapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.movieapp.data.model.FavMovies
import kotlinx.coroutines.flow.Flow

@Dao
interface FavMoviesDao {

    @Query("SELECT * FROM favmovies")
    fun getFavMovies(): Flow<List<FavMovies>>

    @Insert
    suspend fun addMovie(favMovies: FavMovies)

    @Query("DELETE FROM favmovies WHERE imdbID = :imdb_id")
    suspend fun delete(imdb_id: String)

}