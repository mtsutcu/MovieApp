package com.example.movieapp.ui.movielist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.data.model.Movie
import com.example.movieapp.databinding.MovieListItemBinding


class MovieListAdapter(var viewModel: MovieListViewModel) :
    ListAdapter<Movie, MovieListAdapter.MoviesViewHolder>(MovieListUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val binding =
            MovieListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MoviesViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, position)
        }
    }


    class MoviesViewHolder(
        private val binding: MovieListItemBinding,
        var viewModel: MovieListViewModel
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Movie, position: Int) {
            binding.fav = item.fav == 1
            binding.ibMovieItemFav.setOnClickListener {
                if (item.fav == 0) {
                    viewModel.addMovie(item.imdbID)

                } else {
                    viewModel.delete(item.imdbID)
                }
            }
            binding.movie = item
            binding.pageNumber = ((position / 10) + 1).toString()
            if ((position) % 10 == 0) {
                binding.pageNumberText.visibility = View.VISIBLE
            } else {
                binding.pageNumberText.visibility = View.GONE
            }
            binding.movieListItemCardView.setOnClickListener {
                val trans = MovieListFragmentDirections.Companion.toMovieDetail(
                    imdbID = item.imdbID,
                    fav = item.fav
                )
                Navigation.findNavController(it).navigate(trans)
            }
            binding.executePendingBindings()
        }
    }

    class MovieListUtil : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.imdbID == newItem.imdbID
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.Title == newItem.Title
                    && oldItem.Poster == newItem.Poster
                    && oldItem.Year == newItem.Year
                    && oldItem.fav == newItem.fav
        }


    }
}
