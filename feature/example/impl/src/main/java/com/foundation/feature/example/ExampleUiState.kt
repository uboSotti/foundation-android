package com.foundation.feature.example

import com.foundation.core.common.result.Result
import com.foundation.core.model.GithubRepo

/**
 * Example 화면의 UI 상태.
 *
 * 불변 data class로 정의하여 상태 변경은 ViewModel의 [StateFlow.update]를 통해서만 가능하다.
 * [copy]를 사용한 부분 업데이트로 각 데이터 소스의 독립적인 상태 갱신을 유지한다.
 */
data class ExampleUiState(

    /** 앱 마지막 실행 시각. */
    val lastLaunchedAt: Result<Long?> = Result.Loading,

    /** GitHub 레포지토리 정보. */
    val githubRepo: Result<GithubRepo> = Result.Loading,
)
