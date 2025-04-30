package com.hardik.movieapp.cmp.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.hardik.movieapp.cmp.isWeb
import com.hardik.movieapp.cmp.presentation.composables.ErrorItem
import com.hardik.movieapp.cmp.presentation.composables.LoadingItem
import com.hardik.movieapp.cmp.presentation.composables.TrendingItem
import com.hardik.movieapp.cmp.presentation.viewmodels.TrendingMoviesViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TrendingMoviesScreen(
    onClickNavigateToDetails: (Int) -> Unit,
) {
    val trendingMoviesViewModel = koinViewModel<TrendingMoviesViewModel>()
    val pagingState by trendingMoviesViewModel.pagingState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        trendingMoviesViewModel.loadNextPage()
    }

    Box(Modifier.fillMaxSize()) {
        Column {
            LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(if (isWeb()) 10 else 3)) {
                items(pagingState.items.size) { index ->
                    val movie = pagingState.items[index]
                    TrendingItem(
                        movieDomain = movie,
                        onClick = { onClickNavigateToDetails(movie.id) })
                    if (index == pagingState.items.lastIndex) {
                        LaunchedEffect(key1 = Unit) {
                            trendingMoviesViewModel.loadNextPage()
                        }
                    }
                }
                when {
                    pagingState.isLoading && pagingState.items.isEmpty() -> {
                        item { LoadingItem() }
                    }

                    pagingState.isLoading && pagingState.items.isNotEmpty() -> {
                        item { LoadingItem() }
                    }

                    pagingState.error != null && pagingState.items.isEmpty() -> {
                        item {
                            ErrorItem(
                                message = pagingState.error!!.message.toString(),
                                modifier = Modifier.fillMaxSize(),
                                onClickRetry = { trendingMoviesViewModel.retry() }
                            )
                        }
                    }

                    pagingState.error != null && pagingState.items.isNotEmpty() -> {
                        item {
                            ErrorItem(
                                message = pagingState.error!!.message.toString(),
                                onClickRetry = { trendingMoviesViewModel.retry() }
                            )
                        }
                    }
                }
            }
        }
    }
}


