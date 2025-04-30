package com.hardik.movieapp.cmp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hardik.movieapp.cmp.data.model.MovieDomain
import com.hardik.movieapp.cmp.data.model.toDomainModel
import com.hardik.movieapp.cmp.data.repository.MovieRepository
import com.hardik.movieapp.cmp.domain.PagingState
import com.hardik.movieapp.cmp.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TrendingMoviesViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {
    private val _pagingState = MutableStateFlow(PagingState<MovieDomain>())
    val pagingState: StateFlow<PagingState<MovieDomain>> = _pagingState.asStateFlow()

    init {
        loadNextPage()
    }

    fun loadNextPage() {
        viewModelScope.launch {
            if (_pagingState.value.isLoading || !_pagingState.value.hasNextPage) return@launch

            _pagingState.update { it.copy(isLoading = true, error = null) }

            movieRepository.getTrendingMovies(_pagingState.value.currentPage).collect { result ->
                when (result) {
                    is Result.Success -> {
                        val newMovies = result.data.results.toDomainModel()
                        _pagingState.update {
                            it.copy(
                                items = it.items + newMovies,
                                currentPage = it.currentPage + 1,
                                hasNextPage = newMovies.isNotEmpty(),
                                isLoading = false,
                                error = null
                            )
                        }
                    }

                    is Result.Error -> {
                        _pagingState.update {
                            it.copy(
                                isLoading = false,
                                error = result.exception
                            )
                        }
                    }
                }
            }
        }
    }

    fun retry() {
        loadNextPage()
    }
}

