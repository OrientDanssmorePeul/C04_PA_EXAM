package com.example.movieexplorerapp.ui.detail

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.movieexplorerapp.data.model.Movie
import com.example.movieexplorerapp.databinding.FragmentDetailBinding
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {

    private val viewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentDetailBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            if (args.movieId != null) {
                viewModel.fetchMovieDetail(args.movieId!!)
                viewModel.movie.collect { coin ->
                    coin?.let {
                        bindMovieDetail(it)
                    }
                }
            }
        }
    }

    private fun bindMovieDetail(movie: Movie?) {
        if (movie != null)
        binding.run {
            tvTitle.text = movie.title
            tvRating.text = "Rating: ${movie.rating}"
            tvDirector.text = "Director: ${movie.director}"
            tvReleaseDate.text = "Release Date: ${movie.releaseDate}"
            tvLength.text = "Length: ${movie.length}"
            tvOverview.text = movie.overview
        }
    }
}