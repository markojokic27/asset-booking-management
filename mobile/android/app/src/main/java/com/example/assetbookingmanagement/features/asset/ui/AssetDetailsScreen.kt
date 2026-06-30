package com.example.assetbookingmanagement.features.asset.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.assetbookingmanagement.R
import com.example.assetbookingmanagement.core.ui.components.AppButton
import com.example.assetbookingmanagement.core.ui.components.AppLoadingState
import com.example.assetbookingmanagement.core.ui.components.AppMessageState
import com.example.assetbookingmanagement.core.ui.components.StatusBadge

@Composable
fun AssetDetailsScreen(
    assetId: Long,
    onBookClick: () -> Unit,
    viewModel: AssetDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val asset = uiState.asset
    val errorMessageResId = uiState.errorMessageResId
    val unavailableText = stringResource(R.string.common_value_unavailable)
    //Fetch asset details when screen is opened or when assetId changes
    LaunchedEffect(assetId) {
        viewModel.getAssetDetails(assetId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        when {
            uiState.isLoading -> {
                AppLoadingState()
            }

            errorMessageResId != null -> {
                AppMessageState(
                    title = stringResource(R.string.asset_error_load_details_title),
                    message = stringResource(errorMessageResId)
                )
            }

            asset != null -> {
                Text(
                    text = asset.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )


                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    AssetDetailItem(
                        label = stringResource(R.string.common_category),
                        value = uiState.categoryName?.ifBlank { unavailableText } ?: unavailableText
                    )
                    AssetStatusDetailItem(
                        label = stringResource(R.string.common_status),
                        status = asset.status
                    )
                    AssetDetailItem(
                        label = stringResource(R.string.asset_details_location_label),
                        value = asset.location.ifBlank { unavailableText }
                    )
                    AssetDetailItem(
                        label = stringResource(R.string.asset_details_description_label),
                        value = asset.description?.ifBlank { unavailableText } ?: unavailableText
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                AppButton(
                    text = stringResource(R.string.common_book),
                    iconRes = R.drawable.calendar_today_24,
                    enabled = asset.status == "ACTIVE",
                    onClick = onBookClick
                )
            }
        }
    }
}

@Composable
private fun AssetStatusDetailItem(
    label: String,
    status: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.width(84.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f)
        )

        StatusBadge(
            status = status,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
private fun AssetDetailItem(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.width(84.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f)
        )

        Text(
            text = value,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
