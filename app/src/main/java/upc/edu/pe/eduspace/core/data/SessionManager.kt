package upc.edu.pe.eduspace.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("eduspace_session")

@Singleton
class SessionManager @Inject constructor(@param:ApplicationContext private val context: Context) {

    companion object {
        val ADMIN_ID_KEY = intPreferencesKey("admin_id")
        val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
        val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    }

    suspend fun saveSession(adminId: Int, email: String) {
        context.dataStore.edit { preferences ->
            preferences[ADMIN_ID_KEY] = adminId
            preferences[IS_LOGGED_IN_KEY] = true
            preferences[USER_EMAIL_KEY] = email
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    val adminIdFlow: Flow<Int?> = context.dataStore.data.map { preferences ->
        preferences[ADMIN_ID_KEY]
    }

    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN_KEY] ?: false
    }
}

