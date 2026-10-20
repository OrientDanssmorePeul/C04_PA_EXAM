package com.example.movieexplorerapp.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieexplorerapp.data.model.Movie
import com.example.movieexplorerapp.data.repo.MovieRepo
import com.example.movieexplorerapp.data.repo.MovieRepoImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repo: MovieRepoImpl = MovieRepo.getInstance()
) : ViewModel() {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    private val _sortOption = MutableStateFlow(SortOption.ASC)
    private val _sortField = MutableStateFlow(SortField.TITLE)
    private val _searchText = MutableStateFlow("")
    private val _isLoading = MutableStateFlow(false)

    val uiState: StateFlow<HomeUiState> = combine(
        _movies,
        _searchText,
        _sortOption,
        _sortField,
        _isLoading
    ) { items, text, sortOption, sortField, isLoading ->
        if (isLoading) {
            HomeUiState.Loading
        } else {
            val items = filterItems(items, text, sortField, sortOption)
            HomeUiState.Success (
                items,
                items.isEmpty()
            )
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        HomeUiState.Loading
    )

    init {
        fetchMovies()
    }

    fun filterItems(items: List<Movie>, text: String, sortField: SortField, sortOption: SortOption): List<Movie> {
        var filteredItems = items.filter { it.title.contains(text) }
        for (item in items){
            Log.d("DATA", item.releaseDate)
        }
        filteredItems = when(sortOption) {
            SortOption.ASC -> {
                when(sortField) {
                    SortField.TITLE -> filteredItems.sortedBy { it.title }
                    SortField.LENGTH -> filteredItems.sortedBy { it.length }
                    SortField.RATING -> filteredItems.sortedBy { it.rating }
                    SortField.DIRECTOR -> filteredItems.sortedBy { it.director }
                    SortField.OVERVIEW -> filteredItems.sortedBy { it.overview }
                    SortField.RELEASE_DATE -> filteredItems.sortedBy { it.releaseDate }
                }
            }
            SortOption.DESC -> {
                when(sortField) {
                    SortField.TITLE -> filteredItems.sortedByDescending { it.title }
                    SortField.LENGTH -> filteredItems.sortedByDescending { it.length }
                    SortField.RATING -> filteredItems.sortedByDescending { it.rating }
                    SortField.DIRECTOR -> filteredItems.sortedByDescending { it.director }
                    SortField.OVERVIEW -> filteredItems.sortedByDescending { it.overview }
                    SortField.RELEASE_DATE -> filteredItems.sortedByDescending { it.releaseDate }
                }
            }
        }
        return filteredItems
    }

    fun fetchMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.update { true }
            repo.getMovies().let { coins ->
                _movies.update { coins }
            }
            _isLoading.update { false }
        }
    }

    fun onSearchQueryChanged(text: String) {
        _searchText.update { text }
    }
    fun onSortFieldChanged(sortField: SortField) {
        _sortField.update {sortField}
    }
    fun onSortOptionChanged(sortOption: SortOption) {
        _sortOption.update {sortOption}
    }
}

sealed interface HomeUiState {
    object Loading: HomeUiState
    data class Error(val message: String): HomeUiState
    data class Success(val movies: List<Movie>, val isEmpty: Boolean): HomeUiState
}

enum class SortField {
    TITLE, DIRECTOR, OVERVIEW, LENGTH, RATING, RELEASE_DATE
}
enum class SortOption {
    ASC, DESC
}
