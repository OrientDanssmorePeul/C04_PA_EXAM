package com.example.movieexplorerapp.ui.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieexplorerapp.databinding.FragmentHomeBinding
import com.example.movieexplorerapp.ui.adapter.MoviesAdapter
import kotlinx.coroutines.launch
import java.util.Collections

class HomeFragment : Fragment() {

    val sortFields = listOf(
        SortField.TITLE, SortField.DIRECTOR, SortField.OVERVIEW, SortField.LENGTH, SortField.RATING, SortField.RELEASE_DATE
    )
    val sortOptions = listOf(SortOption.ASC, SortOption.DESC)

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: MoviesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        dropDownSetup()
        setupListener()
        lifecycleScope.launch {

            viewModel.uiState.collect {
                if (it is HomeUiState.Success) {
                    binding.pgLoading.visibility = View.INVISIBLE
                    adapter.setMovies(it.movies)
                    if (it.isEmpty) {
                        binding.tvEmpty.visibility = View.VISIBLE
                    } else {
                        binding.tvEmpty.visibility = View.INVISIBLE
                    }
                }
                if (it is HomeUiState.Loading) {
                    binding.pgLoading.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupListener() {
        binding.run {
            iBtnSearch.setOnClickListener {
                viewModel.onSearchQueryChanged(etSearch.text.toString().trim())
            }
            actvdropdownSortBy.doAfterTextChanged {
                viewModel.onSortFieldChanged(SortField.valueOf(it.toString()))
            }
            actvdropdownSortOpt.doAfterTextChanged {
                viewModel.onSortOptionChanged(SortOption.valueOf(it.toString()))
            }
        }
    }
    private fun setupAdapter() {
        adapter = MoviesAdapter(emptyList()) { id ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(id)
            findNavController().navigate(action)
        }
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvMovies.adapter = adapter
        binding.rvMovies.layoutManager = layoutManager
    }

    private fun dropDownSetup() {
        binding.run {
            actvdropdownSortBy.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    sortFields
                )
            )
            actvdropdownSortOpt.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    sortOptions
                )
            )
        }
    }
}