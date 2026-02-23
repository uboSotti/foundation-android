package com.foundation.feature.example

/**
 * Example 화면의 UI 상태를 정의하는 sealed interface.
 *
 * [com.foundation.core.common.result.Result]와 1:1 대응되도록 설계되어
 * ViewModel에서 일관된 상태 매핑을 보장한다.
 */
sealed interface ExampleUiState {

    /** 초기 데이터 로딩 중 상태. */
    data object Loading : ExampleUiState

    /**
     * 데이터 로드 완료 상태.
     *
     * @property lastLaunchedAt 앱 마지막 실행 시각을 포맷한 문자열.
     *                          최초 실행 시에는 null.
     */
    data class Success(
        val lastLaunchedAt: String? = null,
    ) : ExampleUiState

    /**
     * 데이터 로드 실패 상태.
     *
     * @property message 사용자에게 표시할 에러 메시지.
     */
    data class Error(
        val message: String,
    ) : ExampleUiState
}
