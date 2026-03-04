package com.foundation.core.domain.usecase

import com.foundation.core.common.result.Result
import com.foundation.core.domain.repository.GithubRepository
import com.foundation.core.model.GithubRepo
import javax.inject.Inject

/**
 * GitHub 레포지토리 정보를 가져오는 UseCase.
 *
 * [GithubRepository.getRepositoryInfo]를 호출하고
 * [Result]로 래핑하여 반환한다.
 *
 * - [Result.Loading]: 초기 데이터 로딩 중
 * - [Result.Success]: 정상적으로 레포 정보 수신
 * - [Result.Error]: 네트워크 호출 실패
 */
class GetGithubRepoUseCase @Inject constructor(
    private val githubRepository: GithubRepository,
) {
    /** UseCase를 invoke 연산자로 호출한다. */
    suspend operator fun invoke(): Result<GithubRepo> {
        return try {
            val repo = githubRepository.getRepositoryInfo()
            Result.Success(repo)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
