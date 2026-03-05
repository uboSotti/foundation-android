package com.foundation.feature.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foundation.core.domain.usecase.GetGithubRepoUseCase
import com.foundation.core.domain.usecase.GetLastLaunchedAtUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Example 화면의 상태를 관리하는 ViewModel.
 *
 * 각 UseCase의 Flow를 독립적으로 수집하여 [ExampleUiState]의
 * 개별 필드를 갱신한다. 하나의 데이터가 먼저 도착하면
 * 해당 영역만 즉시 UI에 반영된다.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ExampleViewModel @Inject constructor(
    private val getLastLaunchedAt: GetLastLaunchedAtUseCase,
    private val getGithubRepo: GetGithubRepoUseCase,
) : ViewModel() {

    private val refreshTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    val uiState = ExampleUiState()

    init {
        viewModelScope.launch {
            refreshTrigger.onStart { emit(Unit) }
                .flatMapLatest { getLastLaunchedAt() }
                .collect { uiState.lastLaunchedAt = it }
        }
        viewModelScope.launch {
            refreshTrigger.onStart { emit(Unit) }
                .flatMapLatest { getGithubRepo() }
                .collect { uiState.githubRepo = it }
        }
    }

    /** 수동으로 데이터를 다시 불러온다. */
    fun refresh() {
        refreshTrigger.tryEmit(Unit)
    }
}
