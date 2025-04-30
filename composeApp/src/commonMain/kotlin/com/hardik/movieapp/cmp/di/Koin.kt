package com.hardik.movieapp.cmp.di

import com.hardik.movieapp.cmp.data.remote.MoviesService
import com.hardik.movieapp.cmp.data.remote.MoviesServiceImpl
import com.hardik.movieapp.cmp.data.repository.MovieRepository
import com.hardik.movieapp.cmp.data.repository.MovieRepositoryImpl
import com.hardik.movieapp.cmp.presentation.viewmodels.DetailViewModel
import com.hardik.movieapp.cmp.presentation.viewmodels.PopularMoviesViewModel
import com.hardik.movieapp.cmp.presentation.viewmodels.SearchViewModel
import com.hardik.movieapp.cmp.presentation.viewmodels.TrendingMoviesViewModel
import com.hardik.movieapp.cmp.presentation.viewmodels.UpcomingMoviesViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val networkModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
//            install(Logging) {
//                logger = object : Logger {
//                    private val logger = KotlinLogging.logger("KtorClient")
//                    override fun log(message: String) {
//                        logger.debug { message }
//                    }
//                }
//                level = LogLevel.ALL
//            }
        }
    }
    single<MoviesService> { MoviesServiceImpl(get(), "https://api.themoviedb.org/3") }
    single <MovieRepository> { MovieRepositoryImpl(get()) }
}
val viewModelModule = module {
    viewModel {
        PopularMoviesViewModel(get())
    }
    viewModel {
        TrendingMoviesViewModel(get())
    }
    viewModel {
        UpcomingMoviesViewModel(get())
    }
    viewModel {
        DetailViewModel(get())
    }
    viewModel {
        SearchViewModel(get())
    }
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(networkModule, viewModelModule)
    }


