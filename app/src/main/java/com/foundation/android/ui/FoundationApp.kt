package com.foundation.android.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.foundation.core.navigation.FeatureEntryBuilderFactory

/**
 * 앱 전체를 감싸는 최상위 Composable.
 */
@Composable
fun FoundationApp(
    appState: FoundationAppState,
    entryBuilderFactories: Set<@JvmSuppressWildcards FeatureEntryBuilderFactory>,
    onOpenUrl: (String) -> Unit,
) {
    val entryBuilders = remember(entryBuilderFactories, onOpenUrl) {
        entryBuilderFactories.map { factory -> factory.create(onOpenUrl) }
    }

    NavDisplay<NavKey>(
        backStack = appState.backStack,
        onBack = appState::onBack,
        entryProvider = entryProvider {
            entryBuilders.forEach { builder -> this.builder() }
        },
        modifier = Modifier,
    )
}
