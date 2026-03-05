package com.foundation.feature.example

import com.foundation.core.common.error.AppError
import com.foundation.core.common.result.Result
import com.foundation.core.domain.usecase.GetGithubRepoUseCase
import com.foundation.core.domain.usecase.GetLastLaunchedAtUseCase
import com.foundation.core.model.GithubOwner
import com.foundation.core.model.GithubRepo
import com.foundation.core.testing.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

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

        assertTrue(viewModel.uiState.lastLaunchedAt is Result.Loading)
        assertTrue(viewModel.uiState.githubRepo is Result.Loading)
    }

    @Test
    fun `두 UseCase 모두 Success면 각각 Success가 된다`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        val repo = viewModel.uiState.githubRepo
        assertTrue(repo is Result.Success)
        assertEquals("uboSotti/foundation-android", (repo as Result.Success).data.fullName)
        assertEquals("uboSotti", repo.data.owner.login)

        assertTrue(viewModel.uiState.lastLaunchedAt is Result.Success)
    }

    @Test
    fun `GetGithubRepoUseCase가 Error여도 lastLaunchedAt는 독립적으로 Success가 된다`() = runTest {
        val viewModel = createViewModel(
            repoResult = Result.Error(AppError.Network.Connection()),
        )
        advanceUntilIdle()

        val repo = viewModel.uiState.githubRepo
        assertTrue(repo is Result.Error)
        assertTrue((repo as Result.Error).error is AppError.Network.Connection)

        // lastLaunchedAt는 독립적으로 Success
        assertTrue(viewModel.uiState.lastLaunchedAt is Result.Success)
    }

    @Test
    fun `GetLastLaunchedAtUseCase가 Error여도 githubRepo는 독립적으로 Success가 된다`() = runTest {
        val viewModel = createViewModel(
            lastLaunchedResult = Result.Error(AppError.Storage.DataStore()),
        )
        advanceUntilIdle()

        val lastLaunched = viewModel.uiState.lastLaunchedAt
        assertTrue(lastLaunched is Result.Error)
        assertTrue((lastLaunched as Result.Error).error is AppError.Storage.DataStore)

        // githubRepo는 독립적으로 Success
        assertTrue(viewModel.uiState.githubRepo is Result.Success)
    }
}
