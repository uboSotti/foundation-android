package com.foundation.core.network.api

import com.foundation.core.network.model.GithubRepoResponse
import retrofit2.http.GET
import retrofit2.http.Path

/** GitHub REST API 서비스 인터페이스. */
interface GithubApiService {

    /** 레포지토리 정보를 조회한다. */
    @GET("repos/{owner}/{repo}")
    suspend fun getRepositoryInfo(
        @Path("owner") owner: String = "uboSotti",
        @Path("repo") repo: String = "foundation-android",
    ): GithubRepoResponse
}
