package com.foundation.feature.example

import app.cash.turbine.test
import com.foundation.core.common.result.Result
import com.foundation.core.domain.usecase.GetGithubRepoUseCase
import com.foundation.core.domain.usecase.GetLastLaunchedAtUseCase
import com.foundation.core.model.GithubOwner
import com.foundation.core.model.GithubRepo
import com.foundation.core.testing.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
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
    fun `초기 상태는 Loading이다`() = runTest {
        val getLastLaunchedAt = mockk<GetLastLaunchedAtUseCase>()
        val getGithubRepo = mockk<GetGithubRepoUseCase>()
        every { getLastLaunchedAt() } returns flowOf(Result.Loading)
        every { getGithubRepo() } returns flowOf(Result.Loading)

        val viewModel = ExampleViewModel(getLastLaunchedAt, getGithubRepo)

        assertEquals(ExampleUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `두 UseCase 모두 Success면 UiState는 Success가 된다`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            val item = awaitItem()
            if (item is ExampleUiState.Loading) {
                val success = awaitItem()
                assertTrue(success is ExampleUiState.Success)
                val state = success as ExampleUiState.Success
                assertEquals("uboSotti/foundation-android", state.githubRepo.fullName)
                assertEquals("uboSotti", state.githubRepo.owner.login)
            } else {
                assertTrue(item is ExampleUiState.Success)
                val state = item as ExampleUiState.Success
                assertEquals("uboSotti/foundation-android", state.githubRepo.fullName)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `GetGithubRepoUseCase가 Error면 UiState는 Error가 된다`() = runTest {
        val viewModel = createViewModel(
            repoResult = Result.Error(RuntimeException("Network error")),
        )

        viewModel.uiState.test {
            val item = awaitItem()
            if (item is ExampleUiState.Loading) {
                val error = awaitItem()
                assertTrue(error is ExampleUiState.Error)
                assertEquals("Network error", (error as ExampleUiState.Error).message)
            } else {
                assertTrue(item is ExampleUiState.Error)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `GetLastLaunchedAtUseCase가 Error면 UiState는 Error가 된다`() = runTest {
        val viewModel = createViewModel(
            lastLaunchedResult = Result.Error(RuntimeException("DB error")),
        )

        viewModel.uiState.test {
            val item = awaitItem()
            if (item is ExampleUiState.Loading) {
                val error = awaitItem()
                assertTrue(error is ExampleUiState.Error)
                assertEquals("DB error", (error as ExampleUiState.Error).message)
            } else {
                assertTrue(item is ExampleUiState.Error)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }
}
