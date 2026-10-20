package com.example.movieexplorerapp.data.repo

import com.example.movieexplorerapp.data.api.MovieApi
import com.example.movieexplorerapp.data.model.Movie
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class MovieRepoImpl(
    private val api: MovieApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): MovieRepo {
    override suspend fun getMovies(): List<Movie> {
        return api.getMovies()
    }

    override suspend fun getMovie(movieId: Int): Movie {
        return api.getMovie(movieId)
    }
}