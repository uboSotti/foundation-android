package com.foundation.core.common.di

import javax.inject.Qualifier

/**
 * Coroutine Dispatcher를 구분하기 위한 Hilt Qualifier.
 *
 * 직접 `Dispatchers.IO` 등을 호출하지 않고, 이 Qualifier를 통해 주입받아야
 * 테스트 시 'TestDispatcher'로 교체할 수 있다.
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val type: FoundationDispatcher)

enum class FoundationDispatcher {
    IO,
    Default,
    Main,
}
