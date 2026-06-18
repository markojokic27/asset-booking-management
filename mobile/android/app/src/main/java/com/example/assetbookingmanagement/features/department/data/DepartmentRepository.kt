package com.example.assetbookingmanagement.features.department.data

import javax.inject.Inject

class DepartmentRepository @Inject constructor(
    private val departmentApi: DepartmentApi
) {
    suspend fun getDepartmentById(id: Long): DepartmentResponse =
        departmentApi.getDepartmentById(id)
}
