package com.foundation.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.foundation.android.ui.FoundationApp
import com.foundation.android.ui.rememberFoundationAppState
import com.foundation.core.ui.component.ErrorContent
import com.foundation.core.ui.component.LoadingContent
import com.foundation.core.ui.theme.FoundationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            viewModel.uiState.value.shouldKeepSplashScreen()
        }

        // 앱이 포그라운드로 진입하는 시점마다 마지막 실행 시각을 갱신한다.
        // savedInstanceState가 null인 경우에만 최초 진입으로 간주한다.
        if (savedInstanceState == null) {
            viewModel.updateLastLaunchedAt(System.currentTimeMillis())
        }

        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            FoundationTheme {
                when (val state = uiState) {
                    is MainActivityUiState.Configure -> LoadingContent()

                    is MainActivityUiState.Ready -> {
                        val appState = rememberFoundationAppState(
                            isFirstLaunch = state.isFirstLaunch,
                        )
                        FoundationApp(appState = appState)
                    }

                    is MainActivityUiState.Error -> ErrorContent(
                        message = state.exception.message
                            ?: "앱 초기화에 실패했습니다.",
                    )
                }
            }
        }
    }
}
