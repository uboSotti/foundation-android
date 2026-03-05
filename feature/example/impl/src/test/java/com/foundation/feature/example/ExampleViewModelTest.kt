package com.foundation.feature.example

import app.cash.turbine.test
import com.foundation.core.common.error.AppError
import com.foundation.core.common.result.Result
import com.foundation.core.domain.usecase.GetGithubRepoUseCase
import com.foundation.core.domain.usecase.GetLastLaunchedAtUseCase
import com.foundation.core.model.GithubOwner
import com.foundation.core.model.GithubRepo
import com.foundation.core.testing.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExampleViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testRepo = GithubRepo(
        fullName = "uboSotti/foundation-android",
        description = "Android foundation project",
        htmlUrl = "https://github.com/uboSotti/foundation-android",
        updatedAt = "2025-02-24T00:00:00Z",
        stargazersCount = 10,
        forksCount = 2,
        language = "Kotlin",
        owner = GithubOwner(
            login = "uboSotti",
            avatarUrl = "https://avatars.githubusercontent.com/u/123",
            htmlUrl = "https://github.com/uboSotti",
        ),
    )

    private fun createViewModel(
        lastLaunchedResult: Result<Long?> = Result.Success(null),
        repoResult: Result<GithubRepo> = Result.Success(testRepo),
    ): ExampleViewModel {
        val getLastLaunchedAt = mockk<GetLastLaunchedAtUseCase>()
        val getGithubRepo = mockk<GetGithubRepoUseCase>()
        every { getLastLaunchedAt() } returns flowOf(lastLaunchedResult)
        every { getGithubRepo() } returns flowOf(repoResult)
        return ExampleViewModel(getLastLaunchedAt, getGithubRepo)
    }

    @Test
    fun `초기 상태는 Loading이다`() {
        val getLastLaunchedAt = mockk<GetLastLaunchedAtUseCase>()
        val getGithubRepo = mockk<GetGithubRepoUseCase>()
        every { getLastLaunchedAt() } returns flowOf(Result.Loading)
        every { getGithubRepo() } returns flowOf(Result.Loading)

        val viewModel = ExampleViewModel(getLastLaunchedAt, getGithubRepo)

        assertTrue(viewModel.uiState.value.lastLaunchedAt is Result.Loading)
        assertTrue(viewModel.uiState.value.githubRepo is Result.Loading)
    }

    @Test
    fun `두 UseCase 모두 Success면 각각 Success가 된다`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        val repo = viewModel.uiState.value.githubRepo
        assertTrue(repo is Result.Success)
        assertEquals("uboSotti/foundation-android", (repo as Result.Success).data.fullName)
        assertEquals("uboSotti", repo.data.owner.login)

        assertTrue(viewModel.uiState.value.lastLaunchedAt is Result.Success)
    }

    @Test
    fun `GetGithubRepoUseCase가 Error여도 lastLaunchedAt는 독립적으로 Success가 된다`() = runTest {
        val viewModel = createViewModel(
            repoResult = Result.Error(AppError.Network.Connection()),
        )
        advanceUntilIdle()

        val repo = viewModel.uiState.value.githubRepo
        assertTrue(repo is Result.Error)
        assertTrue((repo as Result.Error).error is AppError.Network.Connection)

        // lastLaunchedAt는 독립적으로 Success
        assertTrue(viewModel.uiState.value.lastLaunchedAt is Result.Success)
    }

    @Test
    fun `GetLastLaunchedAtUseCase가 Error여도 githubRepo는 독립적으로 Success가 된다`() = runTest {
        val viewModel = createViewModel(
            lastLaunchedResult = Result.Error(AppError.Storage.DataStore()),
        )
        advanceUntilIdle()

        val lastLaunched = viewModel.uiState.value.lastLaunchedAt
        assertTrue(lastLaunched is Result.Error)
        assertTrue((lastLaunched as Result.Error).error is AppError.Storage.DataStore)

        // githubRepo는 독립적으로 Success
        assertTrue(viewModel.uiState.value.githubRepo is Result.Success)
    }

    @Test
    fun `githubRepo가 Loading 중이면 Refresh intent가 무시된다`() = runTest {
        val getLastLaunchedAt = mockk<GetLastLaunchedAtUseCase>()
        val getGithubRepo = mockk<GetGithubRepoUseCase>()
        every { getLastLaunchedAt() } returns flowOf(Result.Loading)
        every { getGithubRepo() } returns flowOf(Result.Loading)

        val viewModel = ExampleViewModel(getLastLaunchedAt, getGithubRepo)

        // Loading 상태에서 Refresh → 무시되어야 함
        viewModel.onIntent(ExampleIntent.Refresh)
        advanceUntilIdle()

        // 여전히 Loading 상태
        assertTrue(viewModel.uiState.value.githubRepo is Result.Loading)
    }

    @Test
    fun `OpenUrl intent는 OpenBrowser side effect를 발생시킨다`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        val testUrl = "https://github.com/uboSotti/foundation-android"

        viewModel.sideEffect.test {
            viewModel.onIntent(ExampleIntent.OpenUrl(testUrl))

            val effect = awaitItem()
            assertTrue(effect is ExampleSideEffect.OpenBrowser)
            assertEquals(testUrl, (effect as ExampleSideEffect.OpenBrowser).url)
        }
    }
}
