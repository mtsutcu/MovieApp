package com.example.movieapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.data.model.FavMovies
import com.example.movieapp.data.repo.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(val movieRepo: MovieRepository) : ViewModel() {
     var favMovies = MutableLiveData<List<FavMovies>>()


    fun addMovie(id: String) {
        CoroutineScope(Dispatchers.Main).launch {
            movieRepo.addMovie(FavMovies(0, id))

        }
    }

    fun delete(imdbID : String){
        CoroutineScope(Dispatchers.Main).launch {
            movieRepo.delete(imdbID)
        }
    }


}