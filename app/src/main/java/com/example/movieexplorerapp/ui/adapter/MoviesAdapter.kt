package com.example.movieexplorerapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieexplorerapp.data.model.Movie
import com.example.movieexplorerapp.databinding.ItemFragmentMovieBinding

class MoviesAdapter(private var movies: List<Movie>,
                    private val onClick : (id: Int) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class MovieViewHolder(val binding: ItemFragmentMovieBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie, onClick: (id: Int) -> Unit) {
            binding.run {
                tvTitle.text = movie.title
                tvRating.text = "Rating: ${movie.rating}"
                tvDirector.text = "Director: ${movie.director}"
                tvReleaseDate.text = "Release Date: ${movie.releaseDate}"

                itemMovie.setOnClickListener {
                    if (movie.id != null) onClick(movie.id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemFragmentMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = movies[position]
        if (holder is MovieViewHolder) {
            holder.bind(movie, onClick)
        }
    }

    override fun getItemCount(): Int = movies.size

    fun setMovies(newCoins: List<Movie>) {
        movies = newCoins
        notifyDataSetChanged()
    }
}