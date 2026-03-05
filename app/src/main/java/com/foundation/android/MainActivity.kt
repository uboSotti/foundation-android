package com.foundation.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.foundation.android.ui.FoundationApp
import com.foundation.android.ui.rememberFoundationAppState
import com.foundation.core.navigation.FeatureEntryBuilderFactory
import com.foundation.core.navigation.StartDestination
import com.foundation.core.navigation.resolveInitialBackStack
import com.foundation.core.ui.component.ErrorContent
import com.foundation.core.ui.component.LoadingContent
import com.foundation.core.ui.theme.FoundationTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    @Inject
    lateinit var entryBuilderFactories: Set<@JvmSuppressWildcards FeatureEntryBuilderFactory>

    @Inject
    lateinit var startDestinations: Set<@JvmSuppressWildcards StartDestination>

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            viewModel.uiState.value.shouldKeepSplashScreen()
        }

        if (savedInstanceState == null) {
            viewModel.updateLastLaunchedAt(System.currentTimeMillis())
        }

        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            FoundationTheme {
                when (val state = uiState) {
                    is MainActivityUiState.Configure -> LoadingContent(modifier = Modifier.fillMaxSize())

                    is MainActivityUiState.Ready -> {
                        val initialBackStack = remember(startDestinations) {
                            startDestinations.resolveInitialBackStack()
                        }

                        val appState = rememberFoundationAppState(
                            initialBackStack = initialBackStack,
                            isFirstLaunch = state.isFirstLaunch,
                        )
                        FoundationApp(
                            appState = appState,
                            entryBuilderFactories = entryBuilderFactories,
                            onOpenUrl = { url ->
                                startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
                            },
                        )
                    }

                    is MainActivityUiState.Error -> ErrorContent(
                        message = getString(R.string.error_app_init),
                    )
                }
            }
        }
    }
}
