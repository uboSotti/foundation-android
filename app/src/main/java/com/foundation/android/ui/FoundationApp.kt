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
 * @param onOpenUrl 외부 URL 열기 요청 시 호출되는 콜백. Activity에서 Intent 처리를 담당한다.
 */
@Composable
fun FoundationApp(
    appState: FoundationAppState,
    onOpenUrl: (String) -> Unit,
) {
    Scaffold { innerPadding ->
        NavDisplay(
            backStack = appState.backStack,
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            onBack = appState::onBack,
            entryProvider = { key ->
                when (key) {
                    is ExampleNavKey -> exampleEntry(
                        key = key,
                        onOpenUrl = onOpenUrl,
                    )
                    else -> NavEntry(Unit) { }
                }
            },
        )
    }
}
