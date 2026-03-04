package com.foundation.feature.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foundation.core.common.result.Result
import com.foundation.core.domain.usecase.GetGithubRepoUseCase
import com.foundation.core.domain.usecase.GetLastLaunchedAtUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/** Example 화면의 상태를 관리하는 ViewModel. */
@HiltViewModel
class ExampleViewModel @Inject constructor(
    getLastLaunchedAt: GetLastLaunchedAtUseCase,
    getGithubRepo: GetGithubRepoUseCase,
) : ViewModel() {

    /**
     * 두 UseCase의 [Result] Flow를 [combine]하여 [ExampleUiState]로 매핑하는 StateFlow.
     *
     * [SharingStarted.WhileSubscribed]를 사용해 구독자가 없을 때 업스트림을 자동으로
     * 중단하여 리소스를 절약하고, 화면 회전 등 재구독 시 5초 이내라면
     * 마지막 상태를 재사용하여 불필요한 리로딩을 방지한다.
     */
    val uiState: StateFlow<ExampleUiState> = combine(
        getLastLaunchedAt(),
        getGithubRepo(),
    ) { lastLaunchedResult, repoResult ->
        when {
            lastLaunchedResult is Result.Loading || repoResult is Result.Loading ->
                ExampleUiState.Loading

            lastLaunchedResult is Result.Error ->
                ExampleUiState.Error(
                    message = lastLaunchedResult.exception.localizedMessage
                        ?: "알 수 없는 오류가 발생했습니다.",
                )

            repoResult is Result.Error ->
                ExampleUiState.Error(
                    message = repoResult.exception.localizedMessage
                        ?: "알 수 없는 오류가 발생했습니다.",
                )

            lastLaunchedResult is Result.Success && repoResult is Result.Success ->
                ExampleUiState.Success(
                    lastLaunchedAt = lastLaunchedResult.data?.toFormattedDateTime(),
                    githubRepo = repoResult.data,
                )

            else -> ExampleUiState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = ExampleUiState.Loading,
    )

    /**
     * Unix epoch millis를 사람이 읽기 쉬운 날짜/시간 문자열로 변환한다.
     *
     * 예: `2025-07-10 14:30:00`
     */
    private fun Long.toFormattedDateTime(): String =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            .format(Date(this))
}
