package com.foundation.feature.example

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.foundation.core.common.result.Result
import com.foundation.core.model.GithubRepo

/**
 * Example 화면의 UI 상태.
 *
 * 각 데이터 소스를 독립적인 [mutableStateOf]로 관리하여,
 * 하나의 데이터가 먼저 도착하면 해당 영역만 즉시 렌더링된다.
 */
@Stable
class ExampleUiState {

    /** 앱 마지막 실행 시각. */
    var lastLaunchedAt: Result<Long?> by mutableStateOf(Result.Loading)
        internal set

    /** GitHub 레포지토리 정보. */
    var githubRepo: Result<GithubRepo> by mutableStateOf(Result.Loading)
        internal set
}
