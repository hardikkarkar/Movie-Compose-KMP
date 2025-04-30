package com.hardik.movieapp.cmp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.hardik.movieapp.cmp.data.model.MovieDomain
import com.hardik.movieapp.cmp.data.model.toDomainModel
import com.hardik.movieapp.cmp.data.repository.MovieRepository
import com.hardik.movieapp.cmp.utils.getOrThrow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class SearchViewModel (
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: Flow<String> = _searchQuery

    private val movieFlow = MutableStateFlow(emptyList<MovieDomain>())
    fun getMovieFlow(): StateFlow<List<MovieDomain>> = movieFlow

    private val uiStateFlow = MutableStateFlow<String?>(null)
    fun getErrorFlow(): StateFlow<String?> = uiStateFlow

    fun updateQuery(query: String) {
        _searchQuery.value = query
    }

    fun searchMovies(query: String): Flow<Unit> = flow {
        uiStateFlow.emit("Loading...")
        movieRepository.searchMovies(query)
            .catch {
                uiStateFlow.emit(it.message)
            }.collect {
                val result = it.getOrThrow()
                uiStateFlow.emit(null)
                movieFlow.emit(result.results.toDomainModel())
                emit(Unit)
            }
    }.catch {
        uiStateFlow.emit(it.message)
    }

    companion object {
        val TAG = SearchViewModel::class.simpleName
    }
}