package com.foundation.core.model

/** GitHub 레포지토리 소유자 정보를 담는 도메인 모델. */
data class GithubOwner(
    val login: String,
    val avatarUrl: String,
    val htmlUrl: String,
)

/**
 * GitHub 레포지토리의 주요 정보를 담는 도메인 모델.
 *
 * @property fullName 레포지토리 전체 이름 (e.g. "owner/repo").
 * @property description 레포지토리 설명. 없으면 null.
 * @property htmlUrl 브라우저에서 열 수 있는 레포지토리 URL.
 * @property updatedAt 최종 업데이트 일시 (ISO 8601).
 * @property stargazersCount 스타 수.
 * @property forksCount 포크 수.
 * @property language 주요 프로그래밍 언어. 없으면 null.
 * @property owner 레포지토리 소유자 정보.
 */
data class GithubRepo(
    val fullName: String,
    val description: String?,
    val htmlUrl: String,
    val updatedAt: String,
    val stargazersCount: Int,
    val forksCount: Int,
    val language: String?,
    val owner: GithubOwner,
)
