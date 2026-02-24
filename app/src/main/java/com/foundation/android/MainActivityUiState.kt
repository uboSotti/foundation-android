package com.foundation.android

/**
 * [MainActivity]의 앱 수준 UI 상태.
 *
 * AppStartup 단계에서 [Configure]로 시작하여,
 * 초기화 완료 시 [Ready], 오류 발생 시 [Error]로 전환된다.
 */
sealed interface MainActivityUiState {

    /** AppStartup 진행 중. Splash 화면이 이 상태를 커버한다. */
    data object Configure : MainActivityUiState

    /**
     * 앱 초기화 완료.
     *
     * @property isFirstLaunch 앱 최초 실행 여부.
     *                         DataStore에 저장된 마지막 실행 시각이 없으면 true.
     */
    data class Ready(
        val isFirstLaunch: Boolean,
    ) : MainActivityUiState

    /**
     * 초기화 중 오류 발생.
     *
     * @property exception 발생한 예외.
     */
    data class Error(
        val exception: Throwable,
    ) : MainActivityUiState
}

/**
 * Splash 화면 유지 여부.
 *
 * [MainActivityUiState.Configure] 상태일 때만 true를 반환하여
 * 스플래시 화면이 계속 표시되도록 한다.
 */
fun MainActivityUiState.shouldKeepSplashScreen(): Boolean = this is MainActivityUiState.Configure
