package com.example.movieapp.ui.movielist

import androidx.lifecycle.*
import com.example.movieapp.data.model.FavMovies
import com.example.movieapp.data.model.MoviesResponse
import com.example.movieapp.data.repo.MovieRepository
import com.example.movieapp.error.ConsumableError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(var movieRepo: MovieRepository) : ViewModel() {
    private val _viewState = MutableStateFlow(MovieListViewState())
    val viewState: StateFlow<MovieListViewState> = _viewState.asStateFlow()
    var search: String = ""
        set(value) {
            field = value
            fetchPopularMovies(value)
        }
    var page = 1
        set(value) {
            field = value
            fetchMoreMovies(value)
        }
    private var movieRes = MoviesResponse(listOf(), null, null)
    var favMovies = ArrayList<FavMovies>()


    init {
        getFavMovies()
        fetchPopularMovies(search)
    }

    fun addMovie(id: String) {
        CoroutineScope(Dispatchers.Main).launch {
            movieRepo.addMovie(FavMovies(0, id))

        }
    }

    fun delete(imdbID: String) {
        CoroutineScope(Dispatchers.Main).launch {
            movieRepo.delete(imdbID)
        }
    }


    fun getFavMovies() {
        _viewState.update {
            it.copy(
                isLoading = true,
                movieResponse = movieRes,
                favMovieList = favMovies
            )
        }
        viewModelScope.launch {
            movieRepo.getFavMovies().collect { favlist ->
                _viewState.update {
                    it.copy(
                        isLoading = false,
                        movieResponse = movieRes,
                        favMovieList = favlist
                    )
                }
                favMovies = favlist as ArrayList<FavMovies>
            }
        }

    }


    private fun fetchPopularMovies(s: String) {
        _viewState.update {
            it.copy(
                isLoading = true,
            )
        }

        viewModelScope.launch {
            movieRes = movieRepo.searchMovie(s)
            if (!movieRes.Response!!) {
                _viewState.update {
                    it.copy(
                        isLoading = true,
                    )
                }
                delay(1000)
                _viewState.update {
                    it.copy(
                        isLoading = false,
                        movieResponse = movieRes,
                        favMovieList = favMovies
                    )
                }
            } else {
                _viewState.update {
                    it.copy(
                        isLoading = false,
                        movieResponse = movieRes,
                        favMovieList = favMovies
                    )
                }
            }

        }
    }

    private fun fetchMoreMovies(page: Int) {
        _viewState.update {
            it.copy(
                isMoreLoading = true,
            )
        }
        viewModelScope.launch {
            var moreMovie = movieRepo.moreMovies(search, page).Search
            moreMovie.forEach {
                movieRes.Search += it
            }
            delay(100)
            _viewState.update {
                it.copy(
                    isMoreLoading = false,
                    movieResponse = movieRes,
                    favMovieList = favMovies
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


}

data class MovieListViewState(
    val isLoading: Boolean? = false,
    val isMoreLoading: Boolean = false,
    val movieResponse: MoviesResponse? = null,
    val consumableErrors: List<ConsumableError>? = null,
    val favMovieList: List<FavMovies>? = null
)