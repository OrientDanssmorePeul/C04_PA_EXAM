package com.example.movieexplorerapp.data.api

import com.example.movieexplorerapp.data.model.Movie
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieApi {
    @GET("movies")
    suspend fun getMovies(): List<Movie>

    @GET("movies/{movieId}")
    suspend fun getMovie(@Path("movieId") movieId: Int): Movie
}