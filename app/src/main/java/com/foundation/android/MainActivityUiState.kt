package com.foundation.android

/**
 * [MainActivity] 및 [FoundationApp]의 앱 수준 UI 상태.
 *
 * DataStore에서 초기 설정을 로딩하는 동안 [Loading] 상태를 유지하고,
 * 데이터 준비 완료 시 [Success]로 전환된다.
 */
sealed interface MainActivityUiState {

    /** 초기 설정 데이터 로딩 중 상태. */
    data object Loading : MainActivityUiState

    /**
     * 앱 초기 상태 준비 완료.
     *
     * @property isFirstLaunch 앱 최초 실행 여부.
     *                         DataStore에 저장된 마지막 실행 시각이 없으면 true.
     */
    data class Success(
        val isFirstLaunch: Boolean,
    ) : MainActivityUiState
}
