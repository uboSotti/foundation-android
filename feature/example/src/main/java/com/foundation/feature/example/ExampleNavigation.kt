package com.foundation.feature.example

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavEntry
import com.foundation.core.navigation.FeatureEntry
import javax.inject.Inject

/** ':feature:example'의 navigation 계약 구현체. */
class ExampleFeatureEntry @Inject constructor() : FeatureEntry {

    override val startDestination: Any = ExampleNavKey

    override val startDestinationPriority: Int = 0

    override fun entry(
        key: Any,
        onOpenUrl: (String) -> Unit,
    ): NavEntry<Any>? {
        val navKey = key as? ExampleNavKey ?: return null

        return NavEntry(navKey) {
            val viewModel: ExampleViewModel = hiltViewModel()
            ExampleScreen(
                viewModel = viewModel,
                onOpenUrl = onOpenUrl,
            )
        }
    }
}
