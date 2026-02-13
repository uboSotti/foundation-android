package com.foundation.android.ui

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.foundation.core.ui.theme.FoundationTheme
import com.foundation.feature.example.ExampleNavKey
import com.foundation.feature.example.exampleEntry

/** 앱 전체를 감싸는 최상위 Composable. Theme, Scaffold, Navigation을 정의한다. */
@Composable
fun FoundationApp(
    appState: FoundationAppState = rememberFoundationAppState(),
) {
    FoundationTheme {
        Scaffold { innerPadding ->
            FoundationNavHost(
                appState = appState,
                modifier = Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding),
            )
        }
    }
}

@Composable
private fun FoundationNavHost(
    appState: FoundationAppState,
    modifier: Modifier = Modifier,
) {
    NavDisplay(
        backStack = appState.backStack,
        modifier = modifier,
        onBack = { appState.onBack() },
        entryProvider = { key ->
            when (key) {
                is ExampleNavKey -> exampleEntry(key)
                else -> NavEntry(Unit) { }
            }
        },
    )
}
