package com.hardik.movieapp.cmp.data.repository

import com.comet.movieapp.common.Constants.API_KEY
import com.comet.movieapp.common.Constants.LANGUAGE
import com.hardik.movieapp.cmp.data.model.MoviesDetailResponse
import com.hardik.movieapp.cmp.data.model.PopularsMovieResponse
import com.hardik.movieapp.cmp.utils.Result
import com.hardik.movieapp.cmp.data.remote.MoviesService
import com.hardik.movieapp.cmp.utils.performNetworkFlow
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getPopularMovies(page: Int): Flow<Result<PopularsMovieResponse>>
    suspend fun getUpcomingMovies(page: Int): Flow<Result<PopularsMovieResponse>>
    suspend fun getTrendingMovies(page: Int): Flow<Result<PopularsMovieResponse>>
    suspend fun getMovieDetail(movieId: String): Flow<Result<MoviesDetailResponse>>
    suspend fun searchMovies(query: String): Flow<Result<PopularsMovieResponse>>
}

class MovieRepositoryImpl(
    private val moviesService: MoviesService
) : MovieRepository {

    override suspend fun getPopularMovies(page: Int) = performNetworkFlow {
        moviesService.getPopularMovies(API_KEY, LANGUAGE, page)
    }

    override suspend fun getUpcomingMovies(page: Int) = performNetworkFlow {
        moviesService.getUpcomingMovies(API_KEY, LANGUAGE, page)
    }

    override suspend fun getMovieDetail(movieId: String) = performNetworkFlow {
        moviesService.getMovieDetail(movieId, API_KEY, LANGUAGE)
    }

    override suspend fun getTrendingMovies(page: Int) = performNetworkFlow {
        moviesService.getTrendingMovies(API_KEY, LANGUAGE, page)
    }

    override suspend fun searchMovies(query: String) =  performNetworkFlow {
        moviesService.searchMovies(API_KEY, query)
    }
}
