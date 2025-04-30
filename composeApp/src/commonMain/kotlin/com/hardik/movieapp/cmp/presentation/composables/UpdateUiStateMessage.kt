package com.hardik.movieapp.cmp.presentation.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UpdateUiStateMessage(state: State<String?>) {
    if (state.value.isNullOrEmpty().not())
        Text(
            modifier = Modifier.padding(8.dp),
            text = state.value.orEmpty()
        )
}
