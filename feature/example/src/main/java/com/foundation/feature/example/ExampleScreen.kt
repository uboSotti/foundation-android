package com.foundation.feature.example

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.foundation.core.model.GithubRepo
import com.foundation.core.ui.component.ErrorContent
import com.foundation.core.ui.component.LoadingContent

/** Example 화면의 진입점 Composable. ViewModel로부터 상태를 수집하여 [ExampleContent]에 전달한다. */
@Composable
fun ExampleScreen(
    viewModel: ExampleViewModel,
    onOpenUrl: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ExampleContent(
        uiState = uiState,
        onOpenUrl = onOpenUrl,
        modifier = modifier,
    )
}

/**
 * UI 상태에 따라 적절한 화면을 렌더링하는 stateless Composable.
 *
 * - [ExampleUiState.Loading]: 공통 로딩 컴포넌트 표시
 * - [ExampleUiState.Success]: 마지막 실행 시각 정보 + GitHub 레포 카드 표시
 * - [ExampleUiState.Error]: 공통 에러 컴포넌트 표시
 */
@Composable
internal fun ExampleContent(
    uiState: ExampleUiState,
    onOpenUrl: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is ExampleUiState.Loading -> LoadingContent(modifier = modifier)

        is ExampleUiState.Success -> ExampleSuccessContent(
            lastLaunchedAt = uiState.lastLaunchedAt,
            githubRepo = uiState.githubRepo,
            onOpenUrl = onOpenUrl,
            modifier = modifier,
        )

        is ExampleUiState.Error -> ErrorContent(
            message = uiState.message,
            modifier = modifier,
        )
    }
}

@Composable
private fun ExampleSuccessContent(
    lastLaunchedAt: String?,
    githubRepo: GithubRepo,
    onOpenUrl: (String) -> Unit,
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

        Spacer(modifier = Modifier.height(32.dp))

        GithubRepoCard(
            githubRepo = githubRepo,
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
    Card(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = githubRepo.fullName,
                style = MaterialTheme.typography.titleMedium,
            )

            val description = githubRepo.description
            if (description != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.example_owner, githubRepo.owner.login),
                style = MaterialTheme.typography.bodyMedium,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.example_last_updated, githubRepo.updatedAt),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row {
                Text(
                    text = stringResource(R.string.example_stars, githubRepo.stargazersCount),
                    style = MaterialTheme.typography.bodySmall,
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = stringResource(R.string.example_forks, githubRepo.forksCount),
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            val language = githubRepo.language
            if (language != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.example_language, language),
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onOpenUrl(githubRepo.htmlUrl) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.example_open_github_repo))
            }
        }
    }
}
