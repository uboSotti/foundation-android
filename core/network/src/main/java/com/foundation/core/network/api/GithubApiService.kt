package com.foundation.core.network.api

import com.foundation.core.network.model.GithubRepoResponse
import retrofit2.http.GET

/** GitHub REST API 서비스 인터페이스. */
interface GithubApiService {

    /** `uboSotti/foundation-android` 레포지토리 정보를 조회한다. */
    @GET("repos/uboSotti/foundation-android")
    suspend fun getRepositoryInfo(): GithubRepoResponse
}
