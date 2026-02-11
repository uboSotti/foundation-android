package com.foundation.feature.example

/** Example 화면의 UI 상태를 정의하는 sealed interface. */
sealed interface ExampleUiState {

    /** 초기 로딩 상태. */
    data object Loading : ExampleUiState

    /** 기본 상태. */
    data class Success(val message: String) : ExampleUiState
}
