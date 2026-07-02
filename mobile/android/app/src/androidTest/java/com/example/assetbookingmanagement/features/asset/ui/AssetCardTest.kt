package com.example.assetbookingmanagement.features.asset.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.assetbookingmanagement.core.ui.theme.AssetBookingManagementTheme
import com.example.assetbookingmanagement.features.asset.data.AssetResponse
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AssetCardTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testShowsAssetInformationAndStatus() {
        val asset = buildAsset()

        setAssetCard(asset = asset)

        composeRule.onNodeWithText(asset.name).assertIsDisplayed()
        composeRule.onNodeWithText(asset.code ?: "-").assertIsDisplayed()
        composeRule.onNodeWithText(asset.location).assertIsDisplayed()
        composeRule.onNodeWithText(asset.status).assertIsDisplayed()
    }

    @Test
    fun testCallsOnClickWhenCardIsTapped() {
        val asset = buildAsset()
        var clickCalls = 0

        setAssetCard(
            asset = asset,
            onClick = { clickCalls++ }
        )

        composeRule.onNodeWithText(asset.name)
            .assertHasClickAction()
            .performClick()

        assertEquals(1, clickCalls)
    }

    @Test
    fun testShowsDashWhenCodeIsBlank() {
        setAssetCard(asset = buildAsset(code = ""))

        composeRule.onNodeWithText("-").assertIsDisplayed()
    }

    @Test
    fun testShowsDashWhenCodeIsNull() {
        setAssetCard(asset = buildAsset(code = null))

        composeRule.onNodeWithText("-").assertIsDisplayed()
    }

    private fun setAssetCard(
        asset: AssetResponse,
        onClick: () -> Unit = {}
    ) {
        composeRule.setContent {
            AssetBookingManagementTheme {
                AssetCard(
                    asset = asset,
                    onClick = onClick
                )
            }
        }
    }

    private fun buildAsset(
        code: String? = "QR-LAPTOP-001"
    ) = AssetResponse(
        id = 1L,
        name = "Hp 15",
        categoryId = 1L,
        description = "Laptop located in room 301",
        code = code,
        status = "ACTIVE",
        location = "Room 301"
    )
}
