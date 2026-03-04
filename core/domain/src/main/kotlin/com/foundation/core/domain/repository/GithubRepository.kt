package com.foundation.core.domain.repository

import com.foundation.core.model.GithubRepo
import kotlinx.coroutines.flow.Flow

/**
 * GitHub 레포지토리 정보를 제공하는 Repository 인터페이스.
 *
 * 구현체는 `:core:data` 모듈에 위치하며, Hilt를 통해 주입된다.
 */
interface GithubRepository {

    /** 레포지토리 상세 정보를 조회하여 [Flow]로 반환한다. */
    fun getRepositoryInfo(): Flow<GithubRepo>
}
