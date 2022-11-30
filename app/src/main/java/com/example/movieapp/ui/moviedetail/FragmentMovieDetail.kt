package com.example.movieapp.ui.moviedetail

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.movieapp.databinding.FragmentMovieDetailBinding
import com.example.movieapp.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentMovieDetail : Fragment() {
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private val movieDetailViewModel: MovieDetailViewModel by viewModels()
    private val bundle: FragmentMovieDetailArgs by navArgs()
    private var fav = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).setToolbar("", false)

        (requireActivity() as MainActivity).toolbarFavButton.setOnClickListener {
            if (fav == 1) {
                    movieDetailViewModel.viewState.value.movieDetail?.let { it1 ->
                        (requireActivity() as MainActivity).deleteFavMovie(
                            it1.Title, false, bundle.imdbID
                        )
                    }
            } else {
                movieDetailViewModel.viewState.value.movieDetail?.let { it1 ->
                    (requireActivity() as MainActivity).addFavMovie(
                        it1.Title, true, bundle.imdbID
                    )
                }
            }
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
    }


    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                movieDetailViewModel.viewState.collectLatest { movieViewState ->
                    movieViewState.consumableErrors?.let { consumableError ->
                        consumableError.firstOrNull()?.let { error ->
                            Toast.makeText(context, error.exception, Toast.LENGTH_SHORT).show()
                            movieDetailViewModel.errorConsumed(error.id)
                        }
                    }
                    binding.isLoading = movieViewState.isLoading

                    if (movieViewState.movieDetail != null) {
                        binding.movieDetail = movieViewState.movieDetail
                        if (fav == 1) {
                            (requireActivity() as MainActivity).setToolbar(
                                movieViewState.movieDetail.Title,
                                true
                            )

                        } else {
                            (requireActivity() as MainActivity).setToolbar(
                                movieViewState.movieDetail.Title,
                                false
                            )
                        }
                    }
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieDetailViewModel.imdbID = bundle.imdbID
        fav = bundle.fav
    }

}