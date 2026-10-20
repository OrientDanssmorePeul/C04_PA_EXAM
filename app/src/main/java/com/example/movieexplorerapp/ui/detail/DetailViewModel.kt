package com.example.movieexplorerapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieexplorerapp.data.model.Movie
import com.example.movieexplorerapp.data.repo.MovieRepo
import com.example.movieexplorerapp.data.repo.MovieRepoImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repo: MovieRepoImpl = MovieRepo.getInstance()
) : ViewModel() {
    private val _movie = MutableStateFlow<Movie?>(null)
    val movie: StateFlow<Movie?> = _movie
    fun fetchMovieDetail(id: Int) {
        viewModelScope.launch {
            repo.getMovie(id).let { movie ->
                _movie.update { movie }
            }
        }
    }
}