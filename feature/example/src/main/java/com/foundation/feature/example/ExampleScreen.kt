package com.foundation.feature.example

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.foundation.core.ui.component.ErrorContent
import com.foundation.core.ui.component.LoadingContent

/** Example 화면의 진입점 Composable. ViewModel로부터 상태를 수집하여 [ExampleContent]에 전달한다. */
@Composable
fun ExampleScreen(
    viewModel: ExampleViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ExampleContent(uiState = uiState, modifier = modifier)
}

/**
 * UI 상태에 따라 적절한 화면을 렌더링하는 stateless Composable.
 *
 * - [ExampleUiState.Loading]: 공통 로딩 컴포넌트 표시
 * - [ExampleUiState.Success]: 마지막 실행 시각 정보 표시
 * - [ExampleUiState.Error]: 공통 에러 컴포넌트 표시
 */
@Composable
internal fun ExampleContent(
    uiState: ExampleUiState,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is ExampleUiState.Loading -> LoadingContent(modifier = modifier)

        is ExampleUiState.Success -> ExampleSuccessContent(
            lastLaunchedAt = uiState.lastLaunchedAt,
            modifier = modifier,
        )

        is ExampleUiState.Error -> ErrorContent(
            message = uiState.message,
            modifier = modifier,
        )
    }
}

/**
 * 마지막 실행 시각 정보를 표시하는 Composable.
 *
 * 재사용 가능한 단위로 분리되어 있으며, [ExampleContent]에서만 호출된다.
 *
 * @param lastLaunchedAt 포맷된 마지막 실행 시각 문자열. null이면 최초 실행으로 간주한다.
 * @param modifier 외부에서 전달받는 Modifier.
 */
@Composable
private fun ExampleSuccessContent(
    lastLaunchedAt: String?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "여기에 새로운 앱을 시작하세요",
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = if (lastLaunchedAt != null) {
                "마지막 실행: $lastLaunchedAt"
            } else {
                "마지막 실행 기록 없음"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
