package com.foundation.android.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.navigation3.runtime.NavKey

/**
 * [FoundationApp]의 앱 수준 상태를 관리하는 State Holder.
 */
@Stable
class FoundationAppState(
    val backStack: SnapshotStateList<NavKey>,
    isFirstLaunch: Boolean,
) {
    var isFirstLaunch: Boolean by mutableStateOf(isFirstLaunch)
        internal set

    fun onBack() {
        backStack.removeLastOrNull()
    }

    fun navigate(key: NavKey) {
        backStack.add(key)
    }
}

/**
 * [FoundationAppState]를 생성하고 remember 한다.
 */
@Composable
fun rememberFoundationAppState(
    initialBackStack: List<NavKey>,
    isFirstLaunch: Boolean = false,
): FoundationAppState {
    check(initialBackStack.isNotEmpty()) {
        "No start destination is registered. Provide at least one StartDestination."
    }

    return remember(initialBackStack, isFirstLaunch) {
        FoundationAppState(
            backStack = initialBackStack.toMutableStateList(),
            isFirstLaunch = isFirstLaunch,
        )
    }
}
