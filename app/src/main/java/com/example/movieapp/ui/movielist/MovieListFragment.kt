package com.example.movieapp.ui.movielist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.data.model.FavMovies
import com.example.movieapp.data.model.Movie
import com.example.movieapp.databinding.FragmentMovieListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private val movieListAdapter: MovieListAdapter by lazy { MovieListAdapter(this.movieListViewModel) }
    private val movieListViewModel: MovieListViewModel by viewModels()

    lateinit var movielist : ArrayList<Movie>
    var fm = MutableLiveData<List<FavMovies>>()
    var fmID = ArrayList<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)

        binding.btnMovieListSearch?.setOnClickListener {
            searchMovie()
        }

        binding.landscapeSearchButton?.setOnClickListener {
            searchMovie()
        }
        binding.rvMovieList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && movieListViewModel.viewState.value.movieResponse?.Search?.isNotEmpty() == true) {
                    movieListViewModel.page += 1
                }
            }
        })




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvMovieList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = movieListAdapter
        }
        observeData()
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                movieListViewModel.viewState.collectLatest { movieViewState ->
                    movieViewState.consumableErrors?.let { consumableError ->
                        consumableError.firstOrNull()?.let { error ->
                            Toast.makeText(context, error.exception, Toast.LENGTH_SHORT).show()
                            movieListViewModel.errorConsumed(error.id)
                        }
                    }



                    binding.isLoading = movieViewState.isLoading
                    binding.isMoreLoading = movieViewState.isMoreLoading
                    if (movieViewState.movieResponse?.Search != null ) {
                        val searchList = ArrayList<Movie>()
                        val favsID = ArrayList<String>()
                        movieViewState.favMovieList!!.forEach {
                            favsID.add(it.imdbID)
                        }
                        movieViewState.movieResponse.Search.forEach {
                            if (favsID.contains(it.imdbID)){
                                it.fav = 1
                                searchList.add(it)
                            }else{
                                it.fav = 0
                                searchList.add(it)



                            }
                        }

                        movieListAdapter.submitList(movieViewState.movieResponse.Search.map { it.copy() })
                    } else {
                        movieListAdapter.submitList(listOf())
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

    }

    fun searchMovie() {
        var s = binding.etMovieListSearch.text.toString()
        movieListViewModel.search = s
    }
}