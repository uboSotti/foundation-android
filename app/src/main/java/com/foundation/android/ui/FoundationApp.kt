package com.foundation.android.ui

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.foundation.feature.example.ExampleNavKey
import com.foundation.feature.example.exampleEntry

/**
 * 앱 전체를 감싸는 최상위 Composable.
 *
 * [FoundationAppState]를 외부에서 주입받아 Scaffold와 Navigation을 구성한다.
 * ViewModel에 대한 의존 없이 순수한 앱 UI 컴포넌트로 동작한다.
 *
 * @param appState 앱 수준 상태 홀더. [MainActivity]에서 생성하여 전달한다.
 */
@Composable
fun FoundationApp(
    appState: FoundationAppState,
) {
    FoundationScaffold(appState = appState)
}

/**
 * Scaffold와 NavHost를 구성하는 내부 Composable.
 *
 * @param appState 앱 수준 상태 홀더.
 */
@Composable
private fun FoundationScaffold(
    appState: FoundationAppState,
) {
    Scaffold { innerPadding ->
        FoundationDisplay(
            appState = appState,
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
        )
    }
}

@Composable
private fun FoundationDisplay(
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
