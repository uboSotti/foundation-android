package com.foundation.feature.example

/**
 * Example 화면의 사용자 이벤트(Intent).
 *
 * UI에서 발생하는 모든 사용자 액션을 타입으로 정의하여
 * MVI 패턴의 단방향 이벤트 흐름을 보장한다.
 */
sealed interface ExampleIntent {

    /** 데이터를 수동으로 다시 불러온다. */
    data object Refresh : ExampleIntent

    /** 외부 URL을 연다. */
    data class OpenUrl(val url: String) : ExampleIntent
}
