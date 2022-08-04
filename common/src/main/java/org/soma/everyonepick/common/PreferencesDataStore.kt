package org.soma.everyonepick.common

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesDataStore(private val context: Context) {
    companion object {
        const val DATA_STORE_NAME = "app"

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)
        private val accessTokenKey = stringPreferencesKey("access_token")
        private val refreshTokenKey = stringPreferencesKey("refresh_token")
    }

    fun getAccessToken(): Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[accessTokenKey]
    }

    suspend fun editAccessToken(accessToken: String?) {
        context.dataStore.edit { preferences ->
            if (accessToken == null) preferences.remove(accessTokenKey)
            else preferences[accessTokenKey] = accessToken
        }
    }

    fun getRefreshToken(): Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[refreshTokenKey]
    }

    suspend fun editRefreshToken(refreshToken: String?) {
        context.dataStore.edit { preferences ->
            if (refreshToken == null) preferences.remove(refreshTokenKey)
            else preferences[refreshTokenKey] = refreshToken
        }
    }
}