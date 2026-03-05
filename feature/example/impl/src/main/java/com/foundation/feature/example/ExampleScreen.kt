package com.foundation.feature.example

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.foundation.core.common.error.AppError
import com.foundation.core.common.result.Result
import com.foundation.core.model.GithubRepo
import com.foundation.core.ui.component.ErrorContent
import com.foundation.core.ui.component.LoadingContent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Example feature의 route Composable.
 *
 * ViewModel로부터 상태를 수집하고 side effect를 처리한 뒤,
 * 순수 UI인 [ExampleScreen]에 데이터를 전달한다.
 */
@Composable
fun ExampleRoute(
    onOpenUrl: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExampleViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is ExampleSideEffect.OpenBrowser -> onOpenUrl(effect.url)
            }
        }
    }

    ExampleScreen(
        uiState = uiState,
        onIntent = viewModel::onIntent,
        modifier = modifier,
    )
}

/**
 * Example 화면의 순수 UI Composable.
 *
 * ViewModel 타입에 직접 의존하지 않고, 외부에서 주입된 상태와 이벤트 핸들러만 사용한다.
 */
@Composable
fun ExampleScreen(
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

        // 마지막 실행 시각 - 데이터 도착 시 독립적으로 표시
        LastLaunchedText(result = uiState.lastLaunchedAt)

        Spacer(modifier = Modifier.height(32.dp))

        // GitHub 레포 카드 - 독립적으로 로딩/에러/성공 처리
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
    ElevatedCard(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Owner 헤더: 아바타 + 이름
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = githubRepo.owner.avatarUrl,
                    contentDescription = stringResource(
                        R.string.example_owner_avatar_description,
                        githubRepo.owner.login,
                    ),
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = githubRepo.fullName,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = stringResource(R.string.example_owner_handle, githubRepo.owner.login),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            // 설명
            val description = githubRepo.description
            if (description != null) {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(14.dp))

            // 통계 행
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                StatItem(
                    label = stringResource(R.string.example_stat_stars),
                    value = githubRepo.stargazersCount.toString(),
                )
                StatItem(
                    label = stringResource(R.string.example_stat_forks),
                    value = githubRepo.forksCount.toString(),
                )
                val language = githubRepo.language
                if (language != null) {
                    StatItem(
                        label = stringResource(R.string.example_stat_language),
                        value = language,
                    )
                }
            }

            // 업데이트 일시
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.example_last_updated, githubRepo.updatedAt),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
            )

            Spacer(modifier = Modifier.height(18.dp))

            // 외부 링크 버튼
            FilledTonalButton(
                onClick = { onOpenUrl(githubRepo.htmlUrl) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.example_open_github_repo))
            }
        }
    }
}

/** 통계 항목 하나를 렌더링하는 컴포넌트. */
@Composable
private fun StatItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
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
