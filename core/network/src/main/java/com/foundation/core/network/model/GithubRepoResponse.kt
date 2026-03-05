package com.foundation.core.network.model

import com.foundation.core.model.GithubOwner
import com.foundation.core.model.GithubRepo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** GitHub REST API owner 응답 DTO. */
@Serializable
data class GithubOwnerResponse(
    @SerialName("login") val login: String,
    @SerialName("avatar_url") val avatarUrl: String,
    @SerialName("html_url") val htmlUrl: String,
)

/**
 * GitHub REST API `/repos/{owner}/{repo}` 응답 DTO.
 *
 * [ignoreUnknownKeys][kotlinx.serialization.json.Json] 설정에 의해
 * 선언하지 않은 필드는 자동으로 무시된다.
 */
@Serializable
data class GithubRepoResponse(
    @SerialName("full_name") val fullName: String,
    @SerialName("description") val description: String? = null,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("stargazers_count") val stargazersCount: Int = 0,
    @SerialName("forks_count") val forksCount: Int = 0,
    @SerialName("language") val language: String? = null,
    @SerialName("owner") val owner: GithubOwnerResponse,
)

/** 네트워크 DTO를 도메인 모델로 변환한다. */
fun GithubRepoResponse.toDomainModel(): GithubRepo = GithubRepo(
    fullName = fullName,
    description = description,
    htmlUrl = htmlUrl,
    updatedAt = updatedAt,
    stargazersCount = stargazersCount,
    forksCount = forksCount,
    language = language,
    owner = GithubOwner(
        login = owner.login,
        avatarUrl = owner.avatarUrl,
        htmlUrl = owner.htmlUrl,
    ),
)
