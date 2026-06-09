package com.example.assetbookingmanagement.features.asset.ui

import com.example.assetbookingmanagement.features.asset.data.AssetResponse
import org.junit.Assert.assertEquals
import org.junit.Test

class AssetsUiStateTest {

    @Test
    fun testFilteredAssetsWithoutFilters() {
        val assets = listOf(
            asset(id = 1, name = "MacBook Pro 16", categoryId = 1),
            asset(id = 16, name = "Projector Epson", categoryId = 6)
        )

        val state = AssetsUiState(assets = assets)

        assertEquals(assets, state.filteredAssets)
    }

    @Test
    fun testFilteredAssetsWithSearchText() {
        val state = AssetsUiState(
            assets = listOf(
                asset(id = 1, name = "MacBook Pro 16", categoryId = 1),
                asset(id = 16, name = "Projector Epson", categoryId = 6),
                asset(id = 3, name = "Lenovo ThinkPad", categoryId = 1)
            ),
            searchText = "macbook"
        )

        assertEquals(
            listOf(asset(id = 1, name = "MacBook Pro 16", categoryId = 1)),
            state.filteredAssets
        )
    }

    @Test
    fun testFilteredAssetsWithCategoryAndSearchText() {
        val laptop = asset(id = 3, name = "Lenovo ThinkPad", categoryId = 1)
        val anotherLaptop = asset(id = 19, name = "MacBook Air M2", categoryId = 1)
        val projector = asset(id = 16, name = "Projector Epson", categoryId = 6)

        val state = AssetsUiState(
            assets = listOf(laptop, anotherLaptop, projector),
            selectedCategoryIds = setOf(1),
            searchText = "lenovo"
        )

        assertEquals(listOf(laptop), state.filteredAssets)
    }

    @Test
    fun testFilteredAssetsReturnsEmptyWhenNoAssetMatchesBothConditions() {
        val state = AssetsUiState(
            assets = listOf(
                asset(id = 1, name = "MacBook Pro 16", categoryId = 1),
                asset(id = 16, name = "Projector Epson", categoryId = 6)
            ),
            selectedCategoryIds = setOf(1),
            searchText = "projector"
        )

        assertEquals(emptyList<AssetResponse>(), state.filteredAssets)
    }

    private fun asset(
        id: Long,
        name: String,
        categoryId: Long
    ) = AssetResponse(
        id = id,
        name = name,
        categoryId = categoryId,
        description = "Laptop located in room 301",
        code = "QR-LAPTOP-001",
        status = "ACTIVE",
        location = "Room 301"
    )
}
