package com.example.assetbookingmanagement.features.asset.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assetbookingmanagement.features.asset.data.AssetRepository
import com.example.assetbookingmanagement.features.asset.data.AssetResponse
import com.example.assetbookingmanagement.features.assetcategory.data.AssetCategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

data class AssetDetailsUiState(
    val isLoading: Boolean = false,
    val asset: AssetResponse? = null,
    val categoryName: String? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class AssetDetailsViewModel @Inject constructor(
    private val assetRepository: AssetRepository,
    private val assetCategoryRepository: AssetCategoryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AssetDetailsUiState())
    val uiState: StateFlow<AssetDetailsUiState> = _uiState.asStateFlow()

    fun getAssetDetails(assetId: Long) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {
                val asset = assetRepository.getAssetById(assetId)
                val category = assetCategoryRepository.getAssetCategoryById(asset.categoryId)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        asset = asset,
                        categoryName = category.name,
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
                            else -> "Failed to load asset details."
                        }
                    )
                }
            } catch (_: IOException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Unable to connect to the server. Please try again."
                    )
                }
            }
        }
    }
}
