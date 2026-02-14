package com.foundation.android.initializer

import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.startup.Initializer
import timber.log.Timber

/**
 * Timber 로깅 라이브러리를 초기화하는 Initializer.
 *
 * App Startup을 통해 자동으로 실행되며, 디버그 빌드에서만 Timber를 활성화한다.
 */
class TimberInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        val isDebuggable = context.applicationInfo?.flags?.and(ApplicationInfo.FLAG_DEBUGGABLE) != 0
        if (isDebuggable) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
