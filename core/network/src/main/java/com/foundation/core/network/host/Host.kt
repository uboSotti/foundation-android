package com.foundation.core.network.host

/**
 * API 호스트 URL을 제공하는 인터페이스.
 *
 * 서비스별로 다른 호스트를 사용할 때 구현체를 교체하여 주입한다.
 */
interface Host {
    val url: String
}
