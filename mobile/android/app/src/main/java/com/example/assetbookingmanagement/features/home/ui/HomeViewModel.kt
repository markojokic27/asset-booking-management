package com.example.assetbookingmanagement.features.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assetbookingmanagement.features.asset.data.AssetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val assetCount: Int = 0
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val assetRepository: AssetRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        getAssetCount()
    }

    private fun getAssetCount() {
        viewModelScope.launch {
            try {
                val response = assetRepository.getAssets()
                _uiState.update { it.copy(assetCount = response.content.size) }
            } catch (_: Exception) {
                _uiState.update { it.copy(assetCount = 0) }
            }
        }
    }
}
