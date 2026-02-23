package com.foundation.android.ui

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.foundation.android.MainActivityUiState
import com.foundation.android.MainActivityViewModel
import com.foundation.core.ui.component.LoadingContent
import com.foundation.core.ui.theme.FoundationTheme
import com.foundation.feature.example.ExampleNavKey
import com.foundation.feature.example.exampleEntry

/**
 * 앱 전체를 감싸는 최상위 Composable.
 *
 * [MainActivityViewModel]로부터 앱 수준 상태([MainActivityUiState])를 수신하여
 * [FoundationAppState]로 변환 후 하위 컴포넌트에 전달한다(State Hoisting).
 *
 * - [MainActivityUiState.Loading]: 초기 데이터 준비 중 로딩 화면 표시
 * - [MainActivityUiState.Success]: 앱 상태 준비 완료, 정상 화면 진입
 */
@Composable
fun FoundationApp(
    viewModel: MainActivityViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FoundationTheme {
        when (val state = uiState) {
            is MainActivityUiState.Loading -> LoadingContent()

            is MainActivityUiState.Success -> {
                val appState = rememberFoundationAppState(
                    isFirstLaunch = state.isFirstLaunch,
                )
                FoundationScaffold(appState = appState)
            }
        }
    }
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
        FoundationNavHost(
            appState = appState,
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
        )
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
