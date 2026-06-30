package com.example.assetbookingmanagement.features.asset.ui

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assetbookingmanagement.R
import com.example.assetbookingmanagement.features.assetcategory.data.AssetCategoryRepository
import com.example.assetbookingmanagement.features.assetcategory.data.AssetCategoryResponse
import com.example.assetbookingmanagement.features.asset.data.AssetResponse
import com.example.assetbookingmanagement.features.asset.data.AssetRepository
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
    val categories: List<AssetCategoryResponse> = emptyList(),
    val selectedCategoryIds: Set<Long> = emptySet(),
    val searchText: String = "",
    @param:StringRes val errorMessageRes: Int? = null
){
    val filteredAssets: List<AssetResponse>
        get() = assets.filter { asset ->
            val matchesSearch = asset.name.contains(searchText, ignoreCase = true)

            val matchesCategory =
                selectedCategoryIds.isEmpty() || asset.categoryId in selectedCategoryIds

            matchesSearch && matchesCategory
        }
}

@HiltViewModel
class AssetsViewModel @Inject constructor(
    private val assetRepository: AssetRepository,
    private val assetCategoryRepository: AssetCategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AssetsUiState())
    val uiState: StateFlow<AssetsUiState> = _uiState.asStateFlow()
    init {
        getAssets()
        getCategories()
    }

    fun onSearchTextChange(text: String) {
        _uiState.update {
            it.copy(searchText = text)
        }   
    }

    fun onCategoryClick(categoryId: Long) {
        _uiState.update { state ->
            val updatedSelectedCategories =
                if (categoryId in state.selectedCategoryIds) {
                    state.selectedCategoryIds - categoryId
                } else {
                    state.selectedCategoryIds + categoryId
                }

            state.copy(selectedCategoryIds = updatedSelectedCategories)
        }
    }

    fun getAssets() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessageRes = null)
            }

            try {
                val response = assetRepository.getAssets()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        assets = response.content
                    )
                }
            } catch (error: HttpException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessageRes = when (error.code()) {
                            401, 403 -> R.string.asset_error_assets_not_authorized
                            404 -> R.string.asset_error_assets_not_found
                            else -> R.string.asset_error_assets_load_failed
                        }
                    )
                }
            } catch (_: IOException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessageRes = R.string.login_error_server_unreachable
                    )
                }
            }
        }
    }
    private fun getCategories() {
        viewModelScope.launch {
            try {
                val categories = assetCategoryRepository.getAssetCategories()

                _uiState.update {
                    it.copy(categories = categories)
                }
            } catch (error: HttpException) {
                _uiState.update {
                    it.copy(
                        errorMessageRes = when (error.code()) {
                            401, 403 -> R.string.asset_error_categories_not_authorized
                            404 -> R.string.asset_error_categories_not_found
                            else -> R.string.asset_error_categories_load_failed
                        }
                    )
                }
            } catch (_: IOException) {
                _uiState.update {
                    it.copy(errorMessageRes = R.string.login_error_server_unreachable)
                }
            }
        }
    }
}