package com.example.assetbookingmanagement.features.asset.ui

import com.example.assetbookingmanagement.R
import com.example.assetbookingmanagement.features.asset.data.AssetApi
import com.example.assetbookingmanagement.features.asset.data.AssetRepository
import com.example.assetbookingmanagement.features.asset.data.AssetResponse
import com.example.assetbookingmanagement.features.assetcategory.data.AssetCategoryApi
import com.example.assetbookingmanagement.features.assetcategory.data.AssetCategoryPageResponse
import com.example.assetbookingmanagement.features.assetcategory.data.AssetCategoryRepository
import com.example.assetbookingmanagement.features.assetcategory.data.AssetCategoryResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class AssetDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun testGetAssetDetailsLoadsAssetAndCategory() = runTest {
        val fakeAssetApi = FakeAssetApi().apply {
            assetByIdResponse = asset(id = 1, name = "Hp 15", categoryId = 1)
        }
        val fakeCategoryApi = FakeAssetCategoryApi().apply {
            categoryByIdResponse = category(id = 1, name = "Laptops")
        }

        val viewModel = AssetDetailsViewModel(
            assetRepository = AssetRepository(fakeAssetApi),
            assetCategoryRepository = AssetCategoryRepository(fakeCategoryApi)
        )

        viewModel.getAssetDetails(assetId = 1)
        advanceUntilIdle()

        assertEquals(1, fakeAssetApi.getAssetByIdCalls)
        assertEquals(1, fakeCategoryApi.getAssetCategoryByIdCalls)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(fakeAssetApi.assetByIdResponse, viewModel.uiState.value.asset)
        assertEquals("Laptops", viewModel.uiState.value.categoryName)
        assertEquals(null, viewModel.uiState.value.errorMessageResId)
    }

    @Test
    fun testGetAssetDetailsShowsUnauthorizedErrorWhenUserIsNotAllowed() = runTest {
        val fakeAssetApi = FakeAssetApi().apply {
            getAssetByIdException = buildHttpException(403)
        }
        val viewModel = AssetDetailsViewModel(
            assetRepository = AssetRepository(fakeAssetApi),
            assetCategoryRepository = AssetCategoryRepository(FakeAssetCategoryApi())
        )

        viewModel.getAssetDetails(assetId = 1)
        advanceUntilIdle()

        assertEquals(1, fakeAssetApi.getAssetByIdCalls)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(
            R.string.asset_error_not_authorized,
            viewModel.uiState.value.errorMessageResId
        )
    }

    @Test
    fun testGetAssetDetailsShowsNotFoundErrorWhenAssetDoesNotExist() = runTest {
        val fakeAssetApi = FakeAssetApi().apply {
            getAssetByIdException = buildHttpException(404)
        }
        val viewModel = AssetDetailsViewModel(
            assetRepository = AssetRepository(fakeAssetApi),
            assetCategoryRepository = AssetCategoryRepository(FakeAssetCategoryApi())
        )

        viewModel.getAssetDetails(assetId = 1)
        advanceUntilIdle()

        assertEquals(R.string.asset_error_not_found, viewModel.uiState.value.errorMessageResId)
    }

    @Test
    fun testGetAssetDetailsShowsGenericErrorWhenRequestFailsWithUnexpectedStatus() = runTest {
        val fakeAssetApi = FakeAssetApi().apply {
            getAssetByIdException = buildHttpException(500)
        }
        val viewModel = AssetDetailsViewModel(
            assetRepository = AssetRepository(fakeAssetApi),
            assetCategoryRepository = AssetCategoryRepository(FakeAssetCategoryApi())
        )

        viewModel.getAssetDetails(assetId = 1)
        advanceUntilIdle()

        assertEquals(R.string.asset_error_load_details_message, viewModel.uiState.value.errorMessageResId)
    }

    @Test
    fun testGetAssetDetailsShowsBackendErrorWhenServerCannotBeReached() = runTest {
        val fakeAssetApi = FakeAssetApi().apply {
            getAssetByIdException = IOException("Network error")
        }
        val viewModel = AssetDetailsViewModel(
            assetRepository = AssetRepository(fakeAssetApi),
            assetCategoryRepository = AssetCategoryRepository(FakeAssetCategoryApi())
        )

        viewModel.getAssetDetails(assetId = 1)
        advanceUntilIdle()

        assertEquals(R.string.login_error_server_unreachable, viewModel.uiState.value.errorMessageResId)
    }

    @Test
    fun testGetAssetDetailsShowsNotFoundErrorWhenCategoryDoesNotExist() = runTest {
        val fakeAssetApi = FakeAssetApi().apply {
            assetByIdResponse = asset(id = 1, name = "Hp 15", categoryId = 1)
        }
        val fakeCategoryApi = FakeAssetCategoryApi().apply {
            getAssetCategoryByIdException = buildHttpException(404)
        }
        val viewModel = AssetDetailsViewModel(
            assetRepository = AssetRepository(fakeAssetApi),
            assetCategoryRepository = AssetCategoryRepository(fakeCategoryApi)
        )

        viewModel.getAssetDetails(assetId = 1)
        advanceUntilIdle()

        assertEquals(1, fakeAssetApi.getAssetByIdCalls)
        assertEquals(1, fakeCategoryApi.getAssetCategoryByIdCalls)
        assertEquals(R.string.asset_error_not_found, viewModel.uiState.value.errorMessageResId)
    }

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
        var assetByIdResponse: AssetResponse = AssetResponse(
            id = 1,
            name = "Hp 15",
            categoryId = 1,
            description = "Laptop located in room 301",
            code = "QR-LAPTOP-001",
            status = "ACTIVE",
            location = "Room 301"
        )
        var getAssetByIdException: Exception? = null
        var getAssetByIdCalls: Int = 0

        override suspend fun getAssets(page: Int, size: Int) =
            error("getAssets is not used in AssetDetailsViewModel tests.")

        override suspend fun getAssetById(id: Long): AssetResponse {
            getAssetByIdCalls++
            getAssetByIdException?.let { throw it }
            return assetByIdResponse
        }
    }

    private class FakeAssetCategoryApi : AssetCategoryApi {
        var categoryByIdResponse: AssetCategoryResponse = AssetCategoryResponse(
            id = 1,
            name = "Laptops",
            description = "Laptops assets",
            bookingPeriod = "DAY",
            approval = false
        )
        var getAssetCategoryByIdException: Exception? = null
        var getAssetCategoryByIdCalls: Int = 0

        override suspend fun getAssetCategoryById(id: Long): AssetCategoryResponse {
            getAssetCategoryByIdCalls++
            getAssetCategoryByIdException?.let { throw it }
            return categoryByIdResponse
        }

        override suspend fun getAssetCategories(): AssetCategoryPageResponse {
            error("getAssetCategories is not used in AssetDetailsViewModel tests.")
        }
    }
}
