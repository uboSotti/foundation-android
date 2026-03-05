package com.foundation.feature.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foundation.core.common.result.Result
import com.foundation.core.domain.usecase.GetGithubRepoUseCase
import com.foundation.core.domain.usecase.GetLastLaunchedAtUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Example 화면의 상태를 관리하는 ViewModel.
 *
 * MVI 패턴을 따라 [ExampleIntent]를 수신하고, [ExampleUiState]를 갱신하며,
 * 일회성 이벤트는 [ExampleSideEffect]로 전달한다.
 *
 * 각 UseCase의 Flow를 독립적으로 수집하여 [ExampleUiState]의
 * 개별 필드를 [copy]로 갱신한다.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ExampleViewModel @Inject constructor(
    private val getLastLaunchedAt: GetLastLaunchedAtUseCase,
    private val getGithubRepo: GetGithubRepoUseCase,
) : ViewModel() {

    private val refreshTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    private val _uiState = MutableStateFlow(ExampleUiState())
    val uiState: StateFlow<ExampleUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<ExampleSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        viewModelScope.launch {
            refreshTrigger.onStart { emit(Unit) }
                .flatMapLatest { getLastLaunchedAt() }
                .collect { result ->
                    _uiState.update { it.copy(lastLaunchedAt = result) }
                }
        }
        viewModelScope.launch {
            refreshTrigger.onStart { emit(Unit) }
                .flatMapLatest { getGithubRepo() }
                .collect { result ->
                    _uiState.update { it.copy(githubRepo = result) }
                }
        }
    }

    /** UI에서 발생한 사용자 이벤트를 처리한다. */
    fun onIntent(intent: ExampleIntent) {
        when (intent) {
            is ExampleIntent.Refresh -> {
                // 이미 로딩 중이면 중복 요청을 무시한다 (멱등성).
                if (_uiState.value.githubRepo is Result.Loading) return
                refreshTrigger.tryEmit(Unit)
            }
            is ExampleIntent.OpenUrl -> {
                viewModelScope.launch {
                    _sideEffect.send(ExampleSideEffect.OpenBrowser(intent.url))
                }
            }
        }
    }
}
