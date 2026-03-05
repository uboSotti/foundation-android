package com.foundation.core.data.repository

import com.foundation.core.common.error.AppError
import com.foundation.core.common.error.AppException
import com.foundation.core.network.api.GithubApiService
import com.foundation.core.network.model.GithubOwnerResponse
import com.foundation.core.network.model.GithubRepoResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

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
    fun `getRepositoryInfo는 매핑된 도메인 모델을 반환한다`() = runTest {
        val apiService = mockk<GithubApiService>()
        coEvery { apiService.getRepositoryInfo(any(), any()) } returns testResponse

        val repository = GithubRepositoryImpl(apiService)

        val repo = repository.getRepositoryInfo("uboSotti", "foundation-android")

        assertEquals("uboSotti/foundation-android", repo.fullName)
        assertEquals("https://github.com/uboSotti/foundation-android", repo.htmlUrl)
        assertEquals(10, repo.stargazersCount)
        assertEquals(2, repo.forksCount)
        assertEquals("Kotlin", repo.language)
    }

    @Test
    fun `IOException 발생 시 Network Connection 에러로 매핑된다`() = runTest {
        val apiService = mockk<GithubApiService>()
        coEvery { apiService.getRepositoryInfo(any(), any()) } throws IOException("timeout")

        val repository = GithubRepositoryImpl(apiService)

        try {
            repository.getRepositoryInfo("uboSotti", "foundation-android")
            throw AssertionError("Expected AppException")
        } catch (e: AppException) {
            assertTrue(e.error is AppError.Network.Connection)
        }
    }

    @Test
    fun `분류할 수 없는 예외는 safeApiCall을 통과하여 그대로 전파된다`() = runTest {
        val apiService = mockk<GithubApiService>()
        coEvery { apiService.getRepositoryInfo(any(), any()) } throws RuntimeException("unexpected")

        val repository = GithubRepositoryImpl(apiService)

        try {
            repository.getRepositoryInfo("uboSotti", "foundation-android")
            throw AssertionError("Expected RuntimeException")
        } catch (e: RuntimeException) {
            assertEquals("unexpected", e.message)
        }
    }
}
