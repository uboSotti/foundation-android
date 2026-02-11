package com.foundation.feature.example

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/** Example 화면의 진입점 Composable. */
@Composable
fun ExampleScreen(
    viewModel: ExampleViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ExampleContent(uiState = uiState, modifier = modifier)
}

@Composable
internal fun ExampleContent(
    uiState: ExampleUiState,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        when (uiState) {
            is ExampleUiState.Loading -> {
                CircularProgressIndicator()
            }

            is ExampleUiState.Success -> {
                Text(
                    text = uiState.message,
                    style = MaterialTheme.typography.headlineSmall,
                )
            }
        }
    }
}
