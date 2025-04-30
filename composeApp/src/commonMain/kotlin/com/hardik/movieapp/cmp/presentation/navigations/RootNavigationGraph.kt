package com.hardik.movieapp.cmp.presentation.navigations

import SearchScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.hardik.movieapp.cmp.presentation.screens.DetailScreen
import com.hardik.movieapp.cmp.presentation.screens.PopularMoviesScreen
import com.hardik.movieapp.cmp.presentation.screens.TrendingMoviesScreen
import com.hardik.movieapp.cmp.presentation.screens.UpcomingMoviesScreen
import movieapp_cmp.composeapp.generated.resources.Res
import movieapp_cmp.composeapp.generated.resources.ic_favorite
import movieapp_cmp.composeapp.generated.resources.ic_love
import movieapp_cmp.composeapp.generated.resources.ic_movie
import org.jetbrains.compose.resources.DrawableResource

object Graph {
    const val HOME = "home_graph"
    const val NEXT = "next_graph"
}

sealed class Screen(val route: String) {
    object DetailScreen : Screen("detail_screen")
    object SearchScreen : Screen("search_screen")
}

sealed class HomeScreen(val route: String, val icon: DrawableResource, val title: String) {
    object TrendingScreen : HomeScreen("trending_screen", Res.drawable.ic_favorite, "Trending")
    object PopularHomeScreen : HomeScreen("popular_screen", Res.drawable.ic_movie, "Popular")
    object UpcomingHomeScreen : HomeScreen("upcoming_screen", Res.drawable.ic_love, "Upcoming")
}

@Composable
fun homeNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = HomeScreen.TrendingScreen.route,
    ) {
        composable(HomeScreen.TrendingScreen.route) {
            TrendingMoviesScreen(onClickNavigateToDetails = { movieID ->
                navController.navigate(route = Graph.NEXT + "/$movieID")
            })
        }
        composable(HomeScreen.PopularHomeScreen.route) {
            PopularMoviesScreen(onClickNavigateToDetails = { movieID ->
                navController.navigate(route = Graph.NEXT + "/$movieID")
            })
        }
        composable(HomeScreen.UpcomingHomeScreen.route) {
            UpcomingMoviesScreen(onClickNavigateToDetails = { movieID ->
                navController.navigate(route = Graph.NEXT + "/$movieID")
            })
        }
        detailsNavGraph(navController = navController)
    }
}

fun NavGraphBuilder.detailsNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.NEXT + "/{movieId}",
        startDestination = Screen.DetailScreen.route
    ) {
        composable(Screen.DetailScreen.route) {
            val movieId = it.arguments?.getString("movieId") ?: ""
            DetailScreen(navController,movieId)
        }
        composable(Screen.SearchScreen.route) {
            SearchScreen(navController, onClickNavigateToDetails = { movieID ->
                navController.popBackStack()
                navController.navigate(route = Graph.NEXT + "/$movieID")
            })
        }
    }
}
