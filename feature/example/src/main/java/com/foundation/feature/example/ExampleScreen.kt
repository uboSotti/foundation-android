package com.foundation.feature.example

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.foundation.core.common.error.AppError
import com.foundation.core.common.result.Result
import com.foundation.core.model.GithubRepo
import com.foundation.core.ui.component.ErrorContent
import com.foundation.core.ui.component.LoadingContent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Example 화면의 진입점 Composable. */
@Composable
fun ExampleScreen(
    viewModel: ExampleViewModel,
    onOpenUrl: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is ExampleSideEffect.OpenBrowser -> onOpenUrl(effect.url)
            }
        }
    }

    ExampleContent(
        uiState = uiState,
        onIntent = viewModel::onIntent,
        modifier = modifier,
    )
}

/**
 * 각 데이터 소스의 상태에 따라 독립적으로 렌더링하는 stateless Composable.
 *
 * [ExampleUiState.githubRepo]가 로딩/에러/성공을 각각 처리하고,
 * [ExampleUiState.lastLaunchedAt]는 데이터가 준비되면 독립적으로 표시된다.
 */
@Composable
internal fun ExampleContent(
    uiState: ExampleUiState,
    onIntent: (ExampleIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.example_title),
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.height(12.dp))

        // 마지막 실행 시각 — 데이터 도착 시 독립적으로 표시
        LastLaunchedText(result = uiState.lastLaunchedAt)

        Spacer(modifier = Modifier.height(32.dp))

        // GitHub 레포 카드 — 독립적으로 로딩/에러/성공 처리
        GithubRepoSection(
            result = uiState.githubRepo,
            onOpenUrl = { url -> onIntent(ExampleIntent.OpenUrl(url)) },
            onRetry = { onIntent(ExampleIntent.Refresh) },
        )
    }
}

/** 마지막 실행 시각을 표시한다. 로딩/에러 상태에서는 렌더링하지 않는다. */
@Composable
private fun LastLaunchedText(result: Result<Long?>) {
    if (result is Result.Success) {
        val lastLaunchedAt = result.data
        Text(
            text = if (lastLaunchedAt != null) {
                stringResource(R.string.example_last_launched_at, lastLaunchedAt.toFormattedDateTime())
            } else {
                stringResource(R.string.example_last_launched_none)
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/** GitHub 레포 정보를 상태에 따라 로딩/에러/카드로 렌더링한다. */
@Composable
private fun GithubRepoSection(
    result: Result<GithubRepo>,
    onOpenUrl: (String) -> Unit,
    onRetry: () -> Unit,
) {
    when (result) {
        is Result.Loading -> LoadingContent()

        is Result.Error -> ErrorContent(
            message = result.error.toUserMessage(),
            onRetry = onRetry,
        )

        is Result.Success -> GithubRepoCard(
            githubRepo = result.data,
            onOpenUrl = onOpenUrl,
        )
    }
}

@Composable
private fun GithubRepoCard(
    githubRepo: GithubRepo,
    onOpenUrl: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            // 레포 헤더
            Text(
                text = githubRepo.fullName,
                style = MaterialTheme.typography.titleMedium,
            )
            val description = githubRepo.description
            if (description != null) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 소유자 · 통계
            Text(
                text = stringResource(R.string.example_owner, githubRepo.owner.login),
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = stringResource(R.string.example_last_updated, githubRepo.updatedAt),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = stringResource(R.string.example_stars, githubRepo.stargazersCount),
                    style = MaterialTheme.typography.bodySmall,
                )
                Text(
                    text = stringResource(R.string.example_forks, githubRepo.forksCount),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            val language = githubRepo.language
            if (language != null) {
                Text(
                    text = stringResource(R.string.example_language, language),
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 외부 링크 버튼
            Button(
                onClick = { onOpenUrl(githubRepo.htmlUrl) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.example_open_github_repo))
            }
        }
    }
}

/** [AppError] 유형에 따라 사용자에게 보여줄 메시지를 반환한다. */
@Composable
private fun AppError.toUserMessage(): String = when (this) {
    is AppError.Network.Connection -> stringResource(R.string.error_network_connection)
    is AppError.Network.Http -> stringResource(R.string.error_network_http, code)
    is AppError.Network.Serialization -> stringResource(R.string.error_network_serialization)
    is AppError.Storage.DataStore -> stringResource(R.string.error_storage)
    is AppError.Unknown -> stringResource(R.string.example_unknown_error)
}

private fun Long.toFormattedDateTime(): String =
    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        .format(Date(this))
