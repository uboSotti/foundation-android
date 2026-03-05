package com.foundation.feature.example

/**
 * Example 화면의 일회성 이벤트(Side Effect).
 *
 * 상태와 무관하게 한 번만 소비되어야 하는 이벤트를 정의한다.
 * (네비게이션, 토스트, 외부 앱 실행 등)
 */
sealed interface ExampleSideEffect {

    /** 외부 브라우저로 URL을 연다. */
    data class OpenBrowser(val url: String) : ExampleSideEffect
}
