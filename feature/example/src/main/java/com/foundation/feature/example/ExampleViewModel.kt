package com.foundation.feature.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foundation.core.common.result.Result
import com.foundation.core.domain.usecase.GetLastLaunchedAtUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/** Example 화면의 상태를 관리하는 ViewModel. */
@HiltViewModel
class ExampleViewModel @Inject constructor(
    getLastLaunchedAt: GetLastLaunchedAtUseCase,
) : ViewModel() {

    /**
     * UseCase의 [Result] Flow를 [ExampleUiState]로 매핑하여 노출하는 StateFlow.
     *
     * [SharingStarted.WhileSubscribed]를 사용해 구독자가 없을 때 업스트림을 자동으로
     * 중단하여 리소스를 절약하고, 화면 회전 등 재구독 시 5초 이내라면
     * 마지막 상태를 재사용하여 불필요한 리로딩을 방지한다.
     */
    val uiState: StateFlow<ExampleUiState> = getLastLaunchedAt()
        .map { result ->
            when (result) {
                is Result.Loading -> ExampleUiState.Loading
                is Result.Success -> ExampleUiState.Success(
                    lastLaunchedAt = result.data?.toFormattedDateTime(),
                )
                is Result.Error -> ExampleUiState.Error(
                    message = result.exception.localizedMessage
                        ?: "알 수 없는 오류가 발생했습니다.",
                )
            }
        }
        .stateIn(
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
