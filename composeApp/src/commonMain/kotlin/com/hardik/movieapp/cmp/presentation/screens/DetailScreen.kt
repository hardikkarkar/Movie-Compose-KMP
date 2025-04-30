package com.hardik.movieapp.cmp.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.hardik.movieapp.cmp.presentation.composables.DetailContent
import com.hardik.movieapp.cmp.presentation.composables.UpdateUiStateMessage
import com.hardik.movieapp.cmp.presentation.viewmodels.DetailViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavHostController,
    movieId: String
) {
    val detailViewModel = koinViewModel<DetailViewModel>()
    val movieDetailDomainState = detailViewModel.getMovieDetailFlow().collectAsState()
    val uiState = detailViewModel.getErrorFlow().collectAsState()
    LaunchedEffect(Unit) {
        detailViewModel.getMovieDetails(movieId)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Movie App") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Search")
                    }
                }
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(it)) {
            DetailContent(movieDetailDomainState.value)
            UpdateUiStateMessage(state = uiState)
        }
    }
}
