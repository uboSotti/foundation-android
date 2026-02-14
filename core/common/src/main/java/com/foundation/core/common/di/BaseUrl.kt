package com.foundation.core.common.di

import javax.inject.Qualifier

/**
 * Qualifier for injecting the base URL string for network requests.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseUrl
