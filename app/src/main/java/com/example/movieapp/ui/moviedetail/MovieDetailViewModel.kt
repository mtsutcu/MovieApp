package com.example.movieapp.ui.moviedetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.model.MovieDetail
import com.example.movieapp.data.repo.MovieRepository
import com.example.movieapp.error.ConsumableError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(val movieRepo: MovieRepository) : ViewModel() {


    private val _viewState = MutableStateFlow(MovieListViewState())
    val viewState: StateFlow<MovieListViewState> = _viewState.asStateFlow()

    var imdbID = ""
        set(value) {
            field = value
            getMovieDetail()
        }



    private fun getMovieDetail() {

        _viewState.update {
            it.copy(
                isLoading = true,
            )
        }
        viewModelScope.launch {
            val movie = movieRepo.getMovieDetail(imdbID)
            if(!movie.Response){
                _viewState.update {
                    it.copy(
                        isLoading = true,
                    )
                }
                delay(1000)


            }
            _viewState.update {
                it.copy(
                    isLoading = false,
                    movieDetail = movie
                )
            }

        }
    }

    private fun addErrorToList(exception: String?) {
        exception?.let {
            val errorList =
                _viewState.value.consumableErrors?.toMutableList() ?: mutableListOf()
            errorList.add(ConsumableError(exception = it))
            _viewState.value =
                viewState.value.copy(
                    consumableErrors = errorList,
                    isLoading = false
                )
        }
    }

    fun errorConsumed(errorId: Long) {
        _viewState.update { currentUiState ->
            val newConsumableError =
                currentUiState.consumableErrors?.filterNot { it.id == errorId }
            currentUiState.copy(consumableErrors = newConsumableError, isLoading = false)
        }
    }


    data class MovieListViewState(
        val isLoading: Boolean? = false,
        val movieDetail: MovieDetail? = null,
        val consumableErrors: List<ConsumableError>? = null
    )
}