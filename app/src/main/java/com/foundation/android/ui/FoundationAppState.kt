package com.foundation.android.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList

/**
 * [FoundationApp]의 앱 수준 상태를 관리하는 State Holder.
 *
 * Navigation backStack과 앱 전역 상태(첫 실행 여부 등)를 소유하며,
 * Composable 트리의 최상단에서 하위로 전달(State Hoisting)된다.
 *
 * @param backStack Navigation 화면 스택.
 * @param isFirstLaunch 앱 최초 실행 여부. [MainActivityUiState]로부터 주입된다.
 */
@Stable
class FoundationAppState(
    val backStack: SnapshotStateList<Any>,
    isFirstLaunch: Boolean,
) {
    /**
     * 앱 최초 실행 여부.
     *
     * ViewModel의 상태 변화에 따라 Compose 런타임이 자동으로 재구성(recomposition)한다.
     */
    var isFirstLaunch: Boolean by mutableStateOf(isFirstLaunch)
        internal set

    /** backStack에서 마지막 항목을 제거하여 뒤로 이동한다. */
    fun onBack() {
        backStack.removeLastOrNull()
    }

    /** 새로운 화면으로 이동한다. */
    fun navigate(key: Any) {
        backStack.add(key)
    }
}

/**
 * [FoundationAppState]를 생성하고 remember 한다.
 *
 * @param initialBackStack 시작 화면 스택.
 * @param isFirstLaunch ViewModel에서 수신한 최초 실행 여부.
 */
@Composable
fun rememberFoundationAppState(
    initialBackStack: List<Any>,
    isFirstLaunch: Boolean = false,
): FoundationAppState {
    val backStack = initialBackStack.ifEmpty {
        listOf(Unit)
    }

    return remember(backStack, isFirstLaunch) {
        FoundationAppState(
            backStack = backStack.toMutableStateList(),
            isFirstLaunch = isFirstLaunch,
        )
    }
}
