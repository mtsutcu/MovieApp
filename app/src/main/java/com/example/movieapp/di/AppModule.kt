package com.example.movieapp.di

import android.content.Context
import androidx.room.Room
import com.example.movieapp.data.datasource.DataSource
import com.example.movieapp.data.repo.MovieRepository
import com.example.movieapp.retrofit.ApiUtils
import com.example.movieapp.retrofit.MovieDao
import com.example.movieapp.room.MoviesDatabase
import com.example.movieapp.room.FavMoviesDao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDataSource(movieDao: MovieDao, favMoviesDao: FavMoviesDao): DataSource {
        return DataSource(movieDao, favMoviesDao)
    }

    @Provides
    @Singleton
    fun provideMovieDao(): MovieDao {
        return ApiUtils.getMovieDao()
    }

    @Provides
    @Singleton
    fun provideMovieRepository(dataSource: DataSource): MovieRepository {
        return MovieRepository(dataSource)
    }

    @Provides
    @Singleton
    fun provideFavMoviesDao(@ApplicationContext context: Context): FavMoviesDao {
        val db = Room.databaseBuilder(context, MoviesDatabase::class.java, "movies.sqlite")
            .createFromAsset("movies.sqlite").allowMainThreadQueries().build()
        return db.getFavMoviesDao()
    }


}