package com.foundation.feature.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foundation.core.common.result.Result
import com.foundation.core.domain.usecase.GetGithubRepoUseCase
import com.foundation.core.domain.usecase.GetLastLaunchedAtUseCase
import com.foundation.core.model.GithubRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/** Example 화면의 상태를 관리하는 ViewModel. */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ExampleViewModel @Inject constructor(
    private val getLastLaunchedAt: GetLastLaunchedAtUseCase,
    private val getGithubRepo: GetGithubRepoUseCase,
) : ViewModel() {

    private val refreshTrigger = MutableStateFlow(0)

    /**
     * 명시적인 새로고침 트리거([refreshTrigger])와 [combine]을 활용해,
     * 데이터 갱신 시마다 새롭게 UseCase를 호출하여 [ExampleUiState]로 매핑한다.
     *
     * [SharingStarted.WhileSubscribed]를 사용해 구독자가 없을 때 업스트림을 자동으로
     * 중단하여 리소스를 절약하고, 화면 회전 등 재구독 시 5초 이내라면
     * 마지막 상태를 재사용하여 불필요한 리로딩을 방지한다.
     */
    val uiState: StateFlow<ExampleUiState> = refreshTrigger
        .flatMapLatest {
            combine(
                getLastLaunchedAt(),
                fetchGithubRepo(),
            ) { lastLaunchedResult, repoResult ->
                toUiState(lastLaunchedResult, repoResult)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ExampleUiState.Loading,
        )

    /** 수동으로 데이터를 다시 불러온다. */
    fun refresh() {
        refreshTrigger.value += 1
    }

    /** GitHub 레포 정보를 [Result]로 래핑하여 Flow로 방출한다. */
    private fun fetchGithubRepo() = flow<Result<GithubRepo>> {
        emit(Result.Loading)
        emit(getGithubRepo())
    }

    /** 두 UseCase의 [Result]를 [ExampleUiState]로 매핑한다. */
    private fun toUiState(
        lastLaunchedResult: Result<Long?>,
        repoResult: Result<GithubRepo>,
    ): ExampleUiState = when {
        lastLaunchedResult is Result.Loading || repoResult is Result.Loading ->
            ExampleUiState.Loading

        lastLaunchedResult is Result.Error ->
            ExampleUiState.Error(message = lastLaunchedResult.exception.localizedMessage)

        repoResult is Result.Error ->
            ExampleUiState.Error(message = repoResult.exception.localizedMessage)

        lastLaunchedResult is Result.Success && repoResult is Result.Success ->
            ExampleUiState.Success(
                lastLaunchedAt = lastLaunchedResult.data?.toFormattedDateTime(),
                githubRepo = repoResult.data,
            )

        else -> ExampleUiState.Loading
    }

    /**
     * Unix epoch millis를 사람이 읽기 쉬운 날짜/시간 문자열로 변환한다.
     *
     * 예: `2025-07-10 14:30:00`
     */
    private fun Long.toFormattedDateTime(): String =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            .format(Date(this))
}
