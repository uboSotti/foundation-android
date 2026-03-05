package com.foundation.core.navigation

import androidx.navigation3.runtime.NavEntry

/**
 * 각 feature가 앱 navigation에 참여하기 위해 구현하는 계약.
 */
interface FeatureEntry {

    /** 이 feature의 시작 목적지 key. 시작 화면이 아니면 null. */
    val startDestination: Any?
        get() = null

    /** 시작 목적지 우선순위 (작을수록 우선). */
    val startDestinationPriority: Int
        get() = Int.MAX_VALUE

    /** key에 대응하는 [NavEntry]를 생성한다. 매칭되지 않으면 null을 반환한다. */
    fun entry(
        key: Any,
        onOpenUrl: (String) -> Unit,
    ): NavEntry<Any>?
}

/** 등록된 feature들로부터 초기 backStack을 계산한다. */
fun Set<FeatureEntry>.resolveInitialBackStack(): List<Any> =
    asSequence()
        .filter { it.startDestination != null }
        .sortedBy { it.startDestinationPriority }
        .mapNotNull { it.startDestination }
        .toList()

/** key를 처리할 수 있는 feature entry를 찾는다. */
fun Set<FeatureEntry>.findEntry(
    key: Any,
    onOpenUrl: (String) -> Unit,
): NavEntry<Any>? =
    asSequence()
        .mapNotNull { it.entry(key, onOpenUrl) }
        .firstOrNull()
