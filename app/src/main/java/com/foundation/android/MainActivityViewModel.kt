package com.foundation.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foundation.core.common.result.Result
import com.foundation.core.domain.repository.SettingsRepository
import com.foundation.core.domain.usecase.GetLastLaunchedAtUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformWhile
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [MainActivity]의 앱 수준 상태를 관리하는 ViewModel.
 *
 * DataStore의 초기 설정을 로딩하여 [MainActivityUiState]로 변환하고,
 * 앱 첫 실행 여부 등의 상태를 제공한다.
 */
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    getLastLaunchedAt: GetLastLaunchedAtUseCase,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    /**
     * 앱 수준 UI 상태를 노출하는 StateFlow.
     *
     * DataStore 첫 emit 전까지 [MainActivityUiState.Configure]를 유지하여
     * 스플래시 화면이 표시되도록 한다.
     *
     * 앱의 프로세스 생명주기 동안 상태 평가가 단 한 번만 이루어지도록,
     * 로딩 및 최초 응답(성공/에러)까지만 데이터를 수신하고 구독을 중단한다.
     */
    val uiState: StateFlow<MainActivityUiState> = getLastLaunchedAt()
        .map { result ->
            when (result) {
                is Result.Loading -> MainActivityUiState.Configure
                // lastLaunchedAt이 null이면 DataStore에 기록이 없는 최초 실행
                is Result.Success -> MainActivityUiState.Ready(
                    isFirstLaunch = result.data == null,
                )
                is Result.Error -> MainActivityUiState.Error(result.exception)
            }
        }
        .transformWhile { state ->
            emit(state)
            // Configure 상태일 때만 스트림을 유지하고, 그 외의 상태가 되면 스트림 종료
            state is MainActivityUiState.Configure
        }
        .stateIn(
            scope = viewModelScope,
            // 한 번 평가된 UI 상태가 프로세스 종료 시까지 유지되도록 Lazily 사용
            started = SharingStarted.Lazily,
            initialValue = MainActivityUiState.Configure,
        )

    /**
     * 앱이 진입할 때마다 DataStore에 마지막 실행 시각을 갱신한다.
     *
     * 첫 실행 여부(isFirstLaunch) 판단을 보장하기 위해,
     * DataStore의 초기 데이터 평가가 종료된 이후(Configure 상태가 아닐 때)에만
     * 갱신 여부를 결정한다. [MainActivityUiState.Error] 발생 시에는 갱신하지 않는다.
     *
     * @param launchedAt 갱신할 시각 (Unix epoch millis).
     */
    fun updateLastLaunchedAt(launchedAt: Long) {
        viewModelScope.launch {
            // Configure(로딩) 상태가 끝날 때까지 대기
            val state = uiState.first { it !is MainActivityUiState.Configure }
            // 상태가 Ready(성공)인 경우에만 갱신
            if (state is MainActivityUiState.Ready) {
                settingsRepository.updateLastLaunchedAt(launchedAt)
            }
        }
    }
}
