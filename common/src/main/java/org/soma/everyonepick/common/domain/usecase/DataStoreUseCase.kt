package org.soma.everyonepick.common.domain.usecase

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.soma.everyonepick.common.data.RetrofitFactory.Companion.toBearerToken
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

    val hasShownTutorial: Flow<Boolean?> =
        dataStore.data.map { preferences -> preferences[hasTutorialShownKey] }

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

    suspend fun editHasShownTutorial(hasShownTutorial: Boolean) {
        dataStore.edit {
            it[hasTutorialShownKey] = hasShownTutorial
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
    }
}