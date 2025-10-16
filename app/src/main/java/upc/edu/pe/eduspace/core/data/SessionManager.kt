package upc.edu.pe.eduspace.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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
    }

    suspend fun saveAdminId(adminId: Int){
        context.dataStore.edit { preferences ->
            preferences[ADMIN_ID_KEY] = adminId

        }
    }

    val adminIdFlow: Flow<Int?> = context.dataStore.data.map {
        preferences ->
        preferences[ADMIN_ID_KEY]
    }
}

