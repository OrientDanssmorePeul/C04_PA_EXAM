package com.example.movieexplorerapp.data.repo

import com.example.movieexplorerapp.data.api.ApiClient
import com.example.movieexplorerapp.data.model.Movie

interface MovieRepo {
    suspend fun getMovies(): List<Movie>
    suspend fun getMovie(movieId: Int): Movie

    companion object {
        fun getInstance() = MovieRepoImpl(ApiClient.api)
    }
}