package com.foundation.core.data.repository

import app.cash.turbine.test
import com.foundation.core.network.api.GithubApiService
import com.foundation.core.network.model.GithubOwnerResponse
import com.foundation.core.network.model.GithubRepoResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GithubRepositoryImplTest {

    private val testResponse = GithubRepoResponse(
        fullName = "uboSotti/foundation-android",
        description = "Test description",
        htmlUrl = "https://github.com/uboSotti/foundation-android",
        updatedAt = "2025-02-24T00:00:00Z",
        stargazersCount = 10,
        forksCount = 2,
        language = "Kotlin",
        owner = GithubOwnerResponse(
            login = "uboSotti",
            avatarUrl = "https://avatars.githubusercontent.com/u/123",
            htmlUrl = "https://github.com/uboSotti",
        ),
    )

    @Test
    fun `getRepositoryInfo는 매핑된 도메인 모델을 방출한다`() = runTest {
        val apiService = mockk<GithubApiService>()
        coEvery { apiService.getRepositoryInfo() } returns testResponse

        val repository = GithubRepositoryImpl(apiService)

        repository.getRepositoryInfo().test {
            val repo = awaitItem()
            assertEquals("uboSotti/foundation-android", repo.fullName)
            assertEquals("https://github.com/uboSotti/foundation-android", repo.htmlUrl)
            assertEquals(10, repo.stargazersCount)
            assertEquals(2, repo.forksCount)
            assertEquals("Kotlin", repo.language)
            awaitComplete()
        }
    }

    @Test
    fun `API 호출 실패 시 예외가 전파된다`() = runTest {
        val apiService = mockk<GithubApiService>()
        coEvery { apiService.getRepositoryInfo() } throws RuntimeException("API error")

        val repository = GithubRepositoryImpl(apiService)

        repository.getRepositoryInfo().test {
            awaitError()
        }
    }
}
