package com.example.assetbookingmanagement.features.asset.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.assetbookingmanagement.R
import com.example.assetbookingmanagement.core.ui.components.SearchBar
import androidx.compose.foundation.clickable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.assetbookingmanagement.core.ui.components.AppEmptyState
import com.example.assetbookingmanagement.core.ui.components.AppLoadingState
import com.example.assetbookingmanagement.core.ui.components.AppMessageState

@Composable
fun AssetsScreen(
    onAssetClick: (Long) -> Unit = {},
    viewModel: AssetsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var isFilterSheetOpen by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    SearchBar(
                        value = uiState.searchText,
                        onValueChange = viewModel::onSearchTextChange,
                        placeholder = stringResource(R.string.asset_search_placeholder)
                    )
                }

                IconButton(
                    onClick = {
                        isFilterSheetOpen = true
                    } 
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.filter_alt_24),
                        contentDescription = stringResource(R.string.asset_filters_open_content_description),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            when {
                uiState.isLoading -> {
                    AppLoadingState()
                }

                uiState.errorMessageRes != null -> {
                    AppMessageState(
                        title = stringResource(R.string.asset_error_load_assets_title),
                        message = stringResource(uiState.errorMessageRes!!)
                    )
                }

                uiState.filteredAssets.isNotEmpty() -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = uiState.filteredAssets,
                            key = { asset -> asset.id }
                        ) { asset ->
                            AssetCard(
                                asset = asset,
                                onClick = { onAssetClick(asset.id) }
                            )
                        }
                    }
                }
                else -> {
                    AppEmptyState(text = stringResource(R.string.asset_empty_no_assets))
                }
            }
        }

        QrScannerButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            onQrScanned = { scannedValue ->
                val assetId = scannedValue.toLongOrNull()
                if (assetId != null) {
                    onAssetClick(assetId)
                }
            }
        )
        if (isFilterSheetOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f))
                    .clickable {
                        isFilterSheetOpen = false
                    }
            )

            AssetFilterSideSheet(
                modifier = Modifier.align(Alignment.CenterEnd),
                categories = uiState.categories,
                selectedCategoryIds = uiState.selectedCategoryIds,
                onCategoryClick = viewModel::onCategoryClick,
                onCloseClick = {
                    isFilterSheetOpen = false
                }
            )
        }
    }
}