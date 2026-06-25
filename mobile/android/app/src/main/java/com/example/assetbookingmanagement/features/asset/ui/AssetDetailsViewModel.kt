package com.example.assetbookingmanagement.features.asset.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assetbookingmanagement.R
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
    val errorMessageResId: Int? = null
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
                    errorMessageResId = null
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
                        errorMessageResId = null
                    )
                }
            } catch (error: HttpException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessageResId = when (error.code()) {
                            401, 403 -> R.string.asset_error_not_authorized
                            404 -> R.string.asset_error_not_found
                            else -> R.string.asset_error_load_details_message
                        }
                    )
                }
            } catch (_: IOException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessageResId = R.string.login_error_server_unreachable
                    )
                }
            }
        }
    }
}
