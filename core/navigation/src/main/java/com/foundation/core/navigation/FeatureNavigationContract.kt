package com.foundation.core.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey

/**
 * Feature가 시작 목적지를 앱에 등록하기 위한 모델.
 */
data class StartDestination(
    val key: NavKey,
    val priority: Int = Int.MAX_VALUE,
)

/**
 * Feature가 navigation entry를 앱에 등록하기 위한 factory.
 */
fun interface FeatureEntryBuilderFactory {
    fun create(
        onOpenUrl: (String) -> Unit,
    ): EntryProviderScope<NavKey>.() -> Unit
}

/** 등록된 feature start destination들로부터 초기 back stack을 계산한다. */
fun Set<StartDestination>.resolveInitialBackStack(): List<NavKey> =
    asSequence()
        .sortedBy { it.priority }
        .map { it.key }
        .toList()
