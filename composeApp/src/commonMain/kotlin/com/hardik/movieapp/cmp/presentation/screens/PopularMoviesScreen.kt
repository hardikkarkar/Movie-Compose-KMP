package com.hardik.movieapp.cmp.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.hardik.movieapp.cmp.presentation.composables.ErrorItem
import com.hardik.movieapp.cmp.presentation.composables.LoadingItem
import com.hardik.movieapp.cmp.presentation.composables.MovieItem
import com.hardik.movieapp.cmp.presentation.viewmodels.PopularMoviesViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PopularMoviesScreen(
    onClickNavigateToDetails: (Int) -> Unit,
) {
    val popularMoviesViewModel = koinViewModel<PopularMoviesViewModel>()
    val pagingState by popularMoviesViewModel.pagingState.collectAsState()

    Box(Modifier.fillMaxSize()) {
        Column {
            LazyColumn {
                items(pagingState.items.size) { index ->
                    val movie = pagingState.items[index]
                    MovieItem(
                        movieDomain = movie,
                        onClick = { onClickNavigateToDetails(movie.id) })
                    if (index == pagingState.items.lastIndex) {
                        LaunchedEffect(key1 = Unit) {
                            popularMoviesViewModel.loadNextPage()
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
                                modifier = Modifier.fillParentMaxSize(),
                                onClickRetry = { popularMoviesViewModel.retry() }
                            )
                        }
                    }

                    pagingState.error != null && pagingState.items.isNotEmpty() -> {
                        item {
                            ErrorItem(
                                message = pagingState.error!!.message.toString(),
                                onClickRetry = { popularMoviesViewModel.retry() }
                            )
                        }
                    }
                }
            }
        }
    }
}
