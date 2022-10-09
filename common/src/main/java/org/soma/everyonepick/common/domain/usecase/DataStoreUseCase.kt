package org.soma.everyonepick.common.domain.usecase

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreUseCase @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    val bearerAccessToken: Flow<String?> =
        dataStore.data.map { preferences -> preferences[accessTokenKey]?.toBearerToken() }

    val refreshToken: Flow<String?> =
        dataStore.data.map { preferences -> preferences[refreshTokenKey] }

    val hasTutorialShown: Flow<Boolean?> =
        dataStore.data.map { preferences -> preferences[hasTutorialShownKey] }

    val hasSyntheticTutorialShown: Flow<Boolean?> =
        dataStore.data.map { preferences -> preferences[hasSyntheticTutorialShownKey] }

    val hasSyntheticDialogShown: Flow<Boolean?> =
        dataStore.data.map { preferences -> preferences[hasSyntheticDialogShownKey] }

    private fun String.toBearerToken() = "Bearer $this"

    suspend fun editAccessToken(accessToken: String) {
        dataStore.edit {
            it[accessTokenKey] = accessToken
        }
    }

    suspend fun editRefreshToken(refreshToken: String) {
        dataStore.edit {
            it[refreshTokenKey] = refreshToken
        }
    }

    suspend fun editHasTutorialShown(hasTutorialShown: Boolean) {
        dataStore.edit {
            it[hasTutorialShownKey] = hasTutorialShown
        }
    }

    suspend fun editHasSyntheticTutorialShown(hasSyntheticTutorialShown: Boolean) {
        dataStore.edit {
            it[hasSyntheticTutorialShownKey] = hasSyntheticTutorialShown
        }
    }

    suspend fun editHasSyntheticDialogShown(hasSyntheticDialogShown: Boolean) {
        dataStore.edit {
            it[hasSyntheticDialogShownKey] = hasSyntheticDialogShown
        }
    }

    suspend fun removeAccessToken() {
        dataStore.edit {
            it.remove(accessTokenKey)
        }
    }

    suspend fun removeRefreshToken() {
        dataStore.edit {
            it.remove(refreshTokenKey)
        }
    }

    companion object {
        const val DATA_STORE_NAME = "app"

        private val accessTokenKey = stringPreferencesKey("access_token")
        private val refreshTokenKey = stringPreferencesKey("refresh_token")

        private val hasTutorialShownKey = booleanPreferencesKey("has_tutorial_shown")
        private val hasSyntheticDialogShownKey = booleanPreferencesKey("has_synthetic_dialog_shown")
        private val hasSyntheticTutorialShownKey = booleanPreferencesKey("has_synthetic_tutorial_shown")
    }
}