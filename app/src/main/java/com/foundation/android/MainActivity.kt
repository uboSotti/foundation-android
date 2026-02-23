package com.foundation.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.foundation.android.ui.FoundationApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 앱이 포그라운드로 진입하는 시점마다 마지막 실행 시각을 갱신한다.
        // savedInstanceState가 null인 경우에만 최초 진입으로 간주한다.
        if (savedInstanceState == null) {
            viewModel.updateLastLaunchedAt(System.currentTimeMillis())
        }

        enableEdgeToEdge()
        setContent {
            // FoundationApp은 ViewModel의 상태를 구독하여 화면 분기를 담당한다.
            FoundationApp(viewModel = viewModel)
        }
    }
}
