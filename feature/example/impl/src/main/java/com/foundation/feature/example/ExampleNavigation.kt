package com.foundation.feature.example

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey

/** ':feature:example'의 navigation entry builder. */
fun EntryProviderScope<NavKey>.exampleEntryBuilder(
    onOpenUrl: (String) -> Unit,
) {
    entry<ExampleNavKey> {
        ExampleRoute(onOpenUrl = onOpenUrl)
    }
}
