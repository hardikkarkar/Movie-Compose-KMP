package com.hardik.movieapp.cmp.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalMovies
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.hardik.movieapp.cmp.data.model.MovieDetailDomain

@Composable
fun DetailContent(movie: MovieDetailDomain) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(MaterialTheme.shapes.medium),
            model = movie.backdrop_path,
            contentDescription = "Movie Backdrop",
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = movie.title.orEmpty(),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = movie.overview.orEmpty(),
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis
        )
        // Additional Details Section
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Additional Details",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Release Date
        DetailRow(icon = Icons.Filled.DateRange, label = "Release Date") {
            movie.release_date?.let { Text(it) }
        }
        // Runtime
        DetailRow(icon = Icons.Filled.Schedule, label = "Runtime") {
            movie.runtimeWithMinutes?.let { Text(it) }
        }
        // Genres
        DetailRow(icon = Icons.Filled.LocalMovies, label = "Genres") {
            movie.genres?.let { genres ->
                Text(genres.joinToString { it.name.orEmpty() })
            }
        }
        // Rating
        DetailRow(icon = Icons.Filled.Star, label = "Rating") {
            movie.vote_average?.let { Text(it.toString()) }
        }
        // Original Language
        DetailRow(icon = Icons.Filled.Language, label = "Original Language") {
            movie.original_language?.let { Text(it) }
        }
        // Production Companies
        DetailRow(icon = Icons.Filled.LocalMovies, label = "Production Companies") {
            movie.production_companies?.let { companies ->
                Text(companies.joinToString { it.name.orEmpty() })
            }
        }
    }
}

@Composable
fun DetailRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, content: @Composable () -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(modifier = Modifier.padding(start = 32.dp)) {
            content()
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
    }
}