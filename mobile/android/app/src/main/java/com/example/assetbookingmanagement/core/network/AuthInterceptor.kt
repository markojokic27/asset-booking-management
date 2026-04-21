package com.example.assetbookingmanagement.core.network

import com.example.assetbookingmanagement.features.auth.data.AuthSession
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val authSession: AuthSession
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = authSession.accessToken

        val request = if (token.isNullOrBlank()) {
            chain.request()
        } else {
            chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        }

        return chain.proceed(request)
    }
}