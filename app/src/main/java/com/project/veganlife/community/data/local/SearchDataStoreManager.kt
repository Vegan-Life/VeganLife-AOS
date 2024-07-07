package com.project.veganlife.community.data.local

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

private const val PREFERENCES_NAME = "recent_search_preferences"

private val Context.dataStore by preferencesDataStore(
    name = PREFERENCES_NAME
)
class SearchDataStoreManager {
}