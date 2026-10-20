package com.example.movieexplorerapp.data.model

import com.google.gson.annotations.SerializedName


data class Movie(
    val id: Int? = null,
    val title: String,
    val director: String,
    val overview: String,
    val length: Int,
    val rating: Double,
    @SerializedName("release_date")
    val releaseDate: String
)
