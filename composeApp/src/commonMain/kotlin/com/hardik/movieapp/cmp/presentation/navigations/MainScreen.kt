package com.hardik.movieapp.cmp.presentation.navigations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hardik.movieapp.cmp.isWeb

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showTopBar = when (currentRoute) {
        HomeScreen.TrendingScreen.route,
        HomeScreen.PopularHomeScreen.route,
        HomeScreen.UpcomingHomeScreen.route -> true
        else -> false
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (showTopBar) {
                Column {
                    TopAppBar(
                        title = { Text("Movie App") },
                        actions = {
                            IconButton(onClick = {
                                navController.navigate(route = Screen.SearchScreen.route)
                            }) {
                                Icon(Icons.Filled.Search, contentDescription = "Search")
                            }
                        }
                    )
                    if (isWeb())
                        BottomNavigationBar(navController = navController)
                }
            } else {
                Box(modifier = Modifier.height(0.dp))
            }
        },
        bottomBar = {
            if (isWeb().not())
                BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            homeNavGraph(navController)
        }
    }
}
