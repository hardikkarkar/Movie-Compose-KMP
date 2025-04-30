package com.hardik.movieapp.cmp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hardik.movieapp.cmp.data.model.MovieDetailDomain
import com.hardik.movieapp.cmp.data.model.toDomainModel
import com.hardik.movieapp.cmp.data.repository.MovieRepository
import com.hardik.movieapp.cmp.utils.getOrThrow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class DetailViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {
    private val movieDetailFlow = MutableStateFlow(MovieDetailDomain())
    fun getMovieDetailFlow() = movieDetailFlow
    private val uiStateFlow = MutableStateFlow<String?>(null)
    fun getErrorFlow() = uiStateFlow

    fun getMovieDetails(movieId: String) {
        viewModelScope.launch {
            uiStateFlow.emit("Loading...")
            movieRepository.getMovieDetail(movieId)
                .catch {
                    uiStateFlow.emit(it.message)
                }.collect {
                    val result = it.getOrThrow()
                    uiStateFlow.emit(null)
                    movieDetailFlow.emit(result.toDomainModel())
                }
        }
    }
}