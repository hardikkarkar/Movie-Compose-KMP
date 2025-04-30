package com.hardik.movieapp.cmp.data.remote

import com.hardik.movieapp.cmp.data.model.MoviesDetailResponse
import com.hardik.movieapp.cmp.data.model.PopularsMovieResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface MoviesService {
    suspend fun getPopularMovies(apiKey: String, language: String, page: Int): PopularsMovieResponse
    suspend fun getUpcomingMovies(apiKey: String, language: String, page: Int): PopularsMovieResponse
    suspend fun getTrendingMovies(apiKey: String, language: String, page: Int): PopularsMovieResponse
    suspend fun getMovieDetail(id: String, apiKey: String, language: String): MoviesDetailResponse
    suspend fun searchMovies(apiKey: String, query: String): PopularsMovieResponse
}

class MoviesServiceImpl(private val client: HttpClient, private val baseUrl: String) : MoviesService {

    override suspend fun getPopularMovies(apiKey: String, language: String, page: Int): PopularsMovieResponse {
        return client.get("$baseUrl/movie/popular") {
            parameter("api_key", apiKey)
            parameter("language", language)
            parameter("page", page)
        }.body()
    }

    override suspend fun getUpcomingMovies(apiKey: String, language: String, page: Int): PopularsMovieResponse {
        return client.get("$baseUrl/movie/upcoming") {
            parameter("api_key", apiKey)
            parameter("language", language)
            parameter("page", page)
        }.body()
    }

    override suspend fun getTrendingMovies(apiKey:String, language: String, page: Int): PopularsMovieResponse {
        return client.get("$baseUrl/trending/movie/day") {
            parameter("api_key", apiKey)
            parameter("language", language)
            parameter("page", page)
        }.body()
    }

    override suspend fun getMovieDetail(id: String, apiKey: String, language: String): MoviesDetailResponse {
        return client.get("$baseUrl/movie/$id") {
            parameter("api_key", apiKey)
            parameter("language", language)
        }.body()
    }
    override suspend fun searchMovies(apiKey: String, query: String): PopularsMovieResponse {
        return client.get("$baseUrl/search/movie") {
            parameter("api_key", apiKey)
            parameter("query", query)
        }.body()
    }
}
