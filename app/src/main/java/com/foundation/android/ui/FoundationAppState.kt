package com.foundation.android.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.foundation.feature.example.ExampleNavKey

/** [FoundationApp]의 상태를 관리하는 State Holder. Navigation backStack을 소유한다. */
@Stable
class FoundationAppState(
    val backStack: SnapshotStateList<Any>,
) {
    /** backStack에서 마지막 항목을 제거하여 뒤로 이동한다. */
    fun onBack() {
        backStack.removeLastOrNull()
    }

    /** 새로운 화면으로 이동한다. */
    fun navigate(key: Any) {
        backStack.add(key)
    }
}

/** [FoundationAppState]를 생성하고 remember 한다. */
@Composable
fun rememberFoundationAppState(): FoundationAppState {
    return remember {
        FoundationAppState(
            backStack = listOf<Any>(ExampleNavKey).toMutableStateList(),
        )
    }
}
