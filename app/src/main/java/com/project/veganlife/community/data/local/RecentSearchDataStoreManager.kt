package com.project.veganlife.community.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val PREFERENCES_NAME = "recent_search_preferences"

private val Context.dataStore by preferencesDataStore(
    name = PREFERENCES_NAME
)

class RecentSearchDataStoreManager @Inject constructor(@ApplicationContext context: Context) {
    private val dataStore = context.dataStore

    val recentSearch: Flow<List<String>>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_RECENT_SEARCHES]?.let {
                Gson().fromJson(it, Array<String>::class.java).toList()
            } ?: emptyList()
        }


    suspend fun saveRecentSearch(searchList: List<String>) {
        dataStore.edit { preferences ->
            preferences[KEY_RECENT_SEARCHES] = Gson().toJson(searchList)
        }
    }

    companion object {
        private val KEY_RECENT_SEARCHES = stringPreferencesKey("recent_searches")
    }
}