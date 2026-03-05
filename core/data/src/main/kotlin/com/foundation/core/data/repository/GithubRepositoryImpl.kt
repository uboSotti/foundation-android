package com.foundation.core.data.repository

import com.foundation.core.domain.repository.GithubRepository
import com.foundation.core.model.GithubRepo
import com.foundation.core.network.api.GithubApiService
import com.foundation.core.network.model.toDomainModel
import com.foundation.core.network.util.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [GithubRepository]의 구현체.
 *
 * [GithubApiService]를 통해 GitHub REST API를 호출하고,
 * 네트워크 DTO를 도메인 모델로 변환하여 반환한다.
 *
 * [safeApiCall]을 사용하여 네트워크 예외를 [com.foundation.core.common.error.AppError]로 변환한다.
 */
@Singleton
class GithubRepositoryImpl @Inject constructor(
    private val githubApiService: GithubApiService,
) : GithubRepository {

    override suspend fun getRepositoryInfo(owner: String, repo: String): GithubRepo =
        safeApiCall {
            githubApiService.getRepositoryInfo(owner = owner, repo = repo).toDomainModel()
        }
}
