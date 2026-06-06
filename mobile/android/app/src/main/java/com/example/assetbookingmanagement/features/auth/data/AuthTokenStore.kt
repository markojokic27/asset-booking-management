package com.example.assetbookingmanagement.features.auth.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages the storage of the authentication refresh token using Jetpack DataStore
 */

private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

@Singleton
class AuthTokenStore @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val refreshTokenKey = stringPreferencesKey("refresh_token")

    val refreshTokenFlow: Flow<String?> = context.authDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences -> preferences[refreshTokenKey] }

    suspend fun saveRefreshToken(refreshToken: String) {
        context.authDataStore.edit { preferences ->
            preferences[refreshTokenKey] = refreshToken
        }
    }

    suspend fun getRefreshToken(): String? = refreshTokenFlow.map { it }.firstOrNull()

    suspend fun clear() {
        context.authDataStore.edit { preferences ->
            preferences.remove(refreshTokenKey)
        }
    }
}
