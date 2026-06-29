package com.example.assetbookingmanagement.features.department.data

import retrofit2.http.GET
import retrofit2.http.Path

fun interface DepartmentApi {

    @GET("departments/{id}")
    suspend fun getDepartmentById(@Path("id") id: Long): DepartmentResponse
}
