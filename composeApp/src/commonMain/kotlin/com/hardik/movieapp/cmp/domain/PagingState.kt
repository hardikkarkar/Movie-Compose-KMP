package com.hardik.movieapp.cmp.domain

data class PagingState<T>(
    val items: List<T> = emptyList(),
    val currentPage: Int = 1,
    val hasNextPage: Boolean = true,
    val isLoading: Boolean = false,
    val error: Throwable? = null
)