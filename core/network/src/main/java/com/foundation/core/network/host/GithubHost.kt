package com.foundation.core.network.host

import javax.inject.Inject

/** GitHub REST API 호스트. */
class GithubHost @Inject constructor() : Host {
    override val url: String = "https://api.github.com/"
}
