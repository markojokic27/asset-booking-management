package com.example.assetbookingmanagement.features.asset.ui

import com.example.assetbookingmanagement.R
import com.example.assetbookingmanagement.features.asset.data.AssetApi
import com.example.assetbookingmanagement.features.asset.data.AssetListResponse
import com.example.assetbookingmanagement.features.asset.data.AssetRepository
import com.example.assetbookingmanagement.features.asset.data.AssetResponse
import com.example.assetbookingmanagement.features.assetcategory.data.AssetCategoryApi
import com.example.assetbookingmanagement.features.assetcategory.data.AssetCategoryPageResponse
import com.example.assetbookingmanagement.features.assetcategory.data.AssetCategoryRepository
import com.example.assetbookingmanagement.features.assetcategory.data.AssetCategoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class AssetsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun testInitLoadsAssetsAndCategories() = runTest {
        val fakeAssetApi = FakeAssetApi().apply {
            response = AssetListResponse(
                content = listOf(
                    asset(id = 1, name = "Hp 15", categoryId = 1),
                    asset(id = 2, name = "Parking spot 10", categoryId = 2)
                )
            )
        }
        val fakeCategoryApi = FakeAssetCategoryApi().apply {
            response = listOf(
                category(id = 1, name = "Laptops"),
                category(id = 2, name = "Parking")
            )
        }

        val viewModel = AssetsViewModel(
            buildAssetRepository(fakeAssetApi),
            buildAssetCategoryRepository(fakeCategoryApi)
        )
        advanceUntilIdle()

        assertEquals(1, fakeAssetApi.getAssetsCalls)
        assertEquals(1, fakeCategoryApi.getCategoriesCalls)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(fakeAssetApi.response.content, viewModel.uiState.value.assets)
        assertEquals(fakeCategoryApi.response, viewModel.uiState.value.categories)
        assertEquals(null, viewModel.uiState.value.errorMessageRes)
    }

    @Test
    fun testOnSearchTextChangeUpdatesSearchText() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onSearchTextChange("hp")

        assertEquals("hp", viewModel.uiState.value.searchText)
    }

    @Test
    fun testOnCategoryClickTogglesSelectedCategory() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onCategoryClick(1)
        assertEquals(setOf(1L), viewModel.uiState.value.selectedCategoryIds)

        viewModel.onCategoryClick(1)
        assertEquals(emptySet<Long>(), viewModel.uiState.value.selectedCategoryIds)
    }

    @Test
    fun testGetAssetsShowsUnauthorizedErrorWhenUserIsNotAllowed() = runTest {
        val fakeAssetApi = FakeAssetApi().apply {
            getAssetsException = buildHttpException(403)
        }
        val viewModel = AssetsViewModel(
            buildAssetRepository(fakeAssetApi),
            buildAssetCategoryRepository()
        )
        advanceUntilIdle()

        assertEquals(1, fakeAssetApi.getAssetsCalls)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(
            R.string.asset_error_assets_not_authorized,
            viewModel.uiState.value.errorMessageRes
        )
    }

    @Test
    fun testGetAssetsShowsNotFoundErrorWhenAssetsDoNotExist() = runTest {
        val fakeAssetApi = FakeAssetApi().apply {
            getAssetsException = buildHttpException(404)
        }
        val viewModel = AssetsViewModel(
            buildAssetRepository(fakeAssetApi),
            buildAssetCategoryRepository()
        )
        advanceUntilIdle()

        assertEquals(R.string.asset_error_assets_not_found, viewModel.uiState.value.errorMessageRes)
    }

    @Test
    fun testGetAssetsShowsGenericErrorWhenRequestFailsWithUnexpectedStatus() = runTest {
        val fakeAssetApi = FakeAssetApi().apply {
            getAssetsException = buildHttpException(500)
        }
        val viewModel = AssetsViewModel(
            buildAssetRepository(fakeAssetApi),
            buildAssetCategoryRepository()
        )
        advanceUntilIdle()

        assertEquals(R.string.asset_error_assets_load_failed, viewModel.uiState.value.errorMessageRes)
    }

    @Test
    fun testGetAssetsShowsBackendErrorWhenServerCannotBeReached() = runTest {
        val fakeAssetApi = FakeAssetApi().apply {
            getAssetsException = IOException("Network error")
        }
        val viewModel = AssetsViewModel(
            buildAssetRepository(fakeAssetApi),
            buildAssetCategoryRepository()
        )
        advanceUntilIdle()

        assertEquals(R.string.login_error_server_unreachable, viewModel.uiState.value.errorMessageRes)
    }

    @Test
    fun testInitShowsUnauthorizedErrorWhenCategoriesCannotBeViewed() = runTest {
        val fakeCategoryApi = FakeAssetCategoryApi().apply {
            getCategoriesException = buildHttpException(401)
        }
        val viewModel = AssetsViewModel(
            buildAssetRepository(),
            buildAssetCategoryRepository(fakeCategoryApi)
        )
        advanceUntilIdle()

        assertEquals(1, fakeCategoryApi.getCategoriesCalls)
        assertEquals(
            R.string.asset_error_categories_not_authorized,
            viewModel.uiState.value.errorMessageRes
        )
    }

    @Test
    fun testInitShowsNotFoundErrorWhenCategoriesDoNotExist() = runTest {
        val fakeCategoryApi = FakeAssetCategoryApi().apply {
            getCategoriesException = buildHttpException(404)
        }
        val viewModel = AssetsViewModel(
            buildAssetRepository(),
            buildAssetCategoryRepository(fakeCategoryApi)
        )
        advanceUntilIdle()

        assertEquals(R.string.asset_error_categories_not_found, viewModel.uiState.value.errorMessageRes)
    }

    @Test
    fun testInitShowsGenericErrorWhenCategoriesFailWithUnexpectedStatus() = runTest {
        val fakeCategoryApi = FakeAssetCategoryApi().apply {
            getCategoriesException = buildHttpException(500)
        }
        val viewModel = AssetsViewModel(
            buildAssetRepository(),
            buildAssetCategoryRepository(fakeCategoryApi)
        )
        advanceUntilIdle()

        assertEquals(R.string.asset_error_categories_load_failed, viewModel.uiState.value.errorMessageRes)
    }

    @Test
    fun testInitShowsBackendErrorWhenCategoriesCannotReachServer() = runTest {
        val fakeCategoryApi = FakeAssetCategoryApi().apply {
            getCategoriesException = IOException("Network error")
        }
        val viewModel = AssetsViewModel(
            buildAssetRepository(),
            buildAssetCategoryRepository(fakeCategoryApi)
        )
        advanceUntilIdle()

        assertEquals(R.string.login_error_server_unreachable, viewModel.uiState.value.errorMessageRes)
    }

    private fun buildViewModel(
        assetApi: FakeAssetApi = FakeAssetApi(),
        categoryApi: FakeAssetCategoryApi = FakeAssetCategoryApi()
    ) = AssetsViewModel(
        buildAssetRepository(assetApi),
        buildAssetCategoryRepository(categoryApi)
    )

    private fun buildAssetRepository(
        assetApi: FakeAssetApi = FakeAssetApi()
    ) = AssetRepository(assetApi)

    private fun buildAssetCategoryRepository(
        categoryApi: FakeAssetCategoryApi = FakeAssetCategoryApi()
    ) = AssetCategoryRepository(categoryApi)

    private fun buildHttpException(code: Int): HttpException {
        val errorBody = "{}".toResponseBody("application/json".toMediaType())
        return HttpException(Response.error<Any>(code, errorBody))
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

    private fun category(
        id: Long,
        name: String
    ) = AssetCategoryResponse(
        id = id,
        name = name,
        description = "$name assets",
        bookingPeriod = "DAY",
        approval = false
    )

    private class FakeAssetApi : AssetApi {
        var response: AssetListResponse = AssetListResponse(
            content = listOf(
                AssetResponse(
                    id = 1,
                    name = "Hp 15",
                    categoryId = 1,
                    description = "Laptop located in room 301",
                    code = "QR-LAPTOP-001",
                    status = "ACTIVE",
                    location = "Room 301"
                )
            )
        )
        var getAssetsException: Exception? = null
        var getAssetsCalls: Int = 0

        override suspend fun getAssets(page: Int, size: Int): AssetListResponse {
            getAssetsCalls++
            getAssetsException?.let { throw it }
            return response
        }

        override suspend fun getAssetById(id: Long): AssetResponse {
            error("getAssetById is not used in AssetsViewModel tests.")
        }
    }

    private class FakeAssetCategoryApi : AssetCategoryApi {
        var response: List<AssetCategoryResponse> = listOf(
            AssetCategoryResponse(
                id = 1,
                name = "Laptops",
                description = "Laptops assets",
                bookingPeriod = "DAY",
                approval = false
            )
        )
        var getCategoriesException: Exception? = null
        var getCategoriesCalls: Int = 0

        override suspend fun getAssetCategoryById(id: Long): AssetCategoryResponse {
            error("getAssetCategoryById is not used in AssetsViewModel tests.")
        }

        override suspend fun getAssetCategories(): AssetCategoryPageResponse {
            getCategoriesCalls++
            getCategoriesException?.let { throw it }
            return AssetCategoryPageResponse(content = response)
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule : TestWatcher() {
    private val dispatcher = StandardTestDispatcher()

    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
