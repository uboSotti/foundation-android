package com.foundation.feature.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Example 화면의 상태를 관리하는 ViewModel. */
@HiltViewModel
class ExampleViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<ExampleUiState>(ExampleUiState.Loading)

    /** 현재 UI 상태를 노출하는 StateFlow. */
    val uiState: StateFlow<ExampleUiState> = _uiState.asStateFlow()

    init {
        loadInitialState()
    }

    private fun loadInitialState() {
        viewModelScope.launch {
            delay(1_000L)
            _uiState.value = ExampleUiState.Success(
                message = "여기에 새로운 앱을 시작하세요"
            )
        }
    }
}
