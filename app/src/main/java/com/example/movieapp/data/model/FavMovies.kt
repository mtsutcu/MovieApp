package com.example.movieapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "favmovies")
data class FavMovies(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") @NotNull var id : Int,
    @ColumnInfo(name = "imdbID") @NotNull var imdbID : String
)