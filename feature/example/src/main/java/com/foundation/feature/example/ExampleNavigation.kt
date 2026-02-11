package com.foundation.feature.example

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.NavEntry

/**
 * ':feature:example'의 Navigation Entry를 생성한다.
 *
 * @return [ExampleNavKey]에 대응하는 [NavEntry].
 */
fun exampleEntry(key: ExampleNavKey): NavEntry<Any> =
    NavEntry(key) {
        val viewModel: ExampleViewModel = hiltViewModel()
        ExampleScreen(viewModel = viewModel)
    }
