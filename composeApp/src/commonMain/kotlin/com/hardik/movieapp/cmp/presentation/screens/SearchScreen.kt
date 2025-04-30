import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hardik.movieapp.cmp.presentation.composables.SearchItem
import com.hardik.movieapp.cmp.presentation.navigations.Screen
import com.hardik.movieapp.cmp.presentation.viewmodels.SearchViewModel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    onClickNavigateToDetails: (Int) -> Unit,
    searchViewModel: SearchViewModel = koinViewModel<SearchViewModel>()
) {
    val movieFlow by searchViewModel.getMovieFlow().collectAsState(initial = emptyList())
    val uiState by searchViewModel.getErrorFlow().collectAsState(initial = null)
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        searchViewModel.searchQuery.debounce(300).filter { it.isNotBlank() }
            .distinctUntilChanged().flatMapLatest {
                searchViewModel.searchMovies(it)
            }.launchIn(this)
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
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Search")
                    }
                }
            )
        }
    ) {
        Box(Modifier.fillMaxSize().padding(it)) {
            Column {
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    query = query,
                    onQueryChange = {
                        query = it
                        searchViewModel.updateQuery(it)
                    },
                    onSearch = {
                        active = false
                        searchViewModel.searchMovies(it)
                    },
                    active = false,
                    onActiveChange = {
                        active = false
                    },
                    placeholder = { Text("Search Movies") },
                    leadingIcon = {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    },
                    colors = SearchBarDefaults.colors(),
                    content = {}
                )
                Box {
                    when (uiState) {
                        is String -> {
                            Text(
                                text = uiState.toString(),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }

                        null -> {
                            LazyColumn(modifier = Modifier.padding(16.dp)) {
                                items(movieFlow) { movie ->
                                    SearchItem(
                                        movieDomain = movie,
                                        onClick = { onClickNavigateToDetails(movie.id) })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
