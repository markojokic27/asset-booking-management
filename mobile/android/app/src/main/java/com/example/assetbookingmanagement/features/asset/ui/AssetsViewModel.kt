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
    val asset: AssetResponse? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class AssetsViewModel @Inject constructor(
    private val assetRepository: AssetRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AssetsUiState())
    val uiState: StateFlow<AssetsUiState> = _uiState.asStateFlow()

    fun getAssetById(id: Long) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {
                val asset = assetRepository.getAssetById(id)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        asset = asset,
                        errorMessage = null
                    )
                }
            } catch (error: HttpException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = when (error.code()) {
                            401, 403 -> "You are not authorized to view this asset."
                            404 -> "Asset not found."
                            else -> "Failed to load asset."
                        }
                    )
                }
            } catch (_: IOException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Cannot reach backend."
                    )
                }
            }
        }
    }
}