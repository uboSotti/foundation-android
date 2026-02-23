package com.foundation.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foundation.core.common.result.Result
import com.foundation.core.domain.repository.SettingsRepository
import com.foundation.core.domain.usecase.GetLastLaunchedAtUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [MainActivity]의 앱 수준 상태를 관리하는 ViewModel.
 *
 * DataStore의 초기 설정을 로딩하여 [MainActivityUiState]로 변환하고,
 * [com.foundation.android.ui.FoundationApp]에 앱 첫 실행 여부 등의 상태를 제공한다.
 */
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    getLastLaunchedAt: GetLastLaunchedAtUseCase,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    /**
     * 앱 수준 UI 상태를 노출하는 StateFlow.
     *
     * DataStore 첫 emit 전까지 [MainActivityUiState.Loading]을 유지하여
     * 스플래시 화면 또는 로딩 UI를 표시할 수 있도록 한다.
     */
    val uiState: StateFlow<MainActivityUiState> = getLastLaunchedAt()
        .map { result ->
            when (result) {
                is Result.Loading -> MainActivityUiState.Loading
                // lastLaunchedAt이 null이면 DataStore에 기록이 없는 최초 실행
                is Result.Success -> MainActivityUiState.Success(
                    isFirstLaunch = result.data == null,
                )
                // 읽기 오류 시에도 앱이 중단되지 않도록 기본값으로 진입
                is Result.Error -> MainActivityUiState.Success(
                    isFirstLaunch = false,
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = MainActivityUiState.Loading,
        )

    /**
     * 앱이 진입할 때마다 DataStore에 마지막 실행 시각을 갱신한다.
     *
     * @param launchedAt 갱신할 시각 (Unix epoch millis).
     */
    fun updateLastLaunchedAt(launchedAt: Long) {
        viewModelScope.launch {
            settingsRepository.updateLastLaunchedAt(launchedAt)
        }
    }
}
