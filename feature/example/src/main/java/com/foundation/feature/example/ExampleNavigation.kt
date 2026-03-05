package com.foundation.feature.example

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavEntry

/**
 * ':feature:example'의 Navigation Entry를 생성한다.
 *
 * @param onOpenUrl 외부 URL 열기 요청 시 호출되는 콜백. 앱 수준에서 Intent 처리를 담당한다.
 * @return [ExampleNavKey]에 대응하는 [NavEntry].
 */
fun exampleEntry(
    key: ExampleNavKey,
    onOpenUrl: (String) -> Unit = {},
): NavEntry<Any> =
    NavEntry(key) {
        val viewModel: ExampleViewModel = hiltViewModel()
        ExampleScreen(
            viewModel = viewModel,
            onOpenUrl = onOpenUrl,
        )
    }
