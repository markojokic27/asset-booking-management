package com.example.assetbookingmanagement.features.asset.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assetbookingmanagement.features.asset.data.AssetRepository
import com.example.assetbookingmanagement.features.asset.data.AssetResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

data class AssetsUiState(
    val isLoading: Boolean = false,
    val assets: List<AssetResponse> = emptyList(),
    val searchText: String = "",
    val errorMessage: String? = null
){
    val filteredAssets: List<AssetResponse>
        get() = assets.filter { asset ->
            asset.name.contains(searchText, ignoreCase = true)
        }
}

@HiltViewModel
class AssetsViewModel @Inject constructor(
    private val assetRepository: AssetRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AssetsUiState())
    val uiState: StateFlow<AssetsUiState> = _uiState.asStateFlow()
    init {
        getAssets()
    }

    fun onSearchTextChange(text: String) {
    _uiState.update {
        it.copy(searchText = text)
    }
}

    fun getAssets() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            try {
                val response = assetRepository.getAssets()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        assets = response.content
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error loading assets"
                    )
                }
            }
        }
    }
}