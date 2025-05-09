package com.tiyasinsania0090.wishlystack.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore : DataStore<Preferences> by preferencesDataStore( name = "settings_preference")
class SettingDataStore(private val context: Context) {

    // Key untuk menyimpan preferensi
    companion object {
        private val IS_LIST = booleanPreferencesKey("is_list")
    }

    // Mengambil data layout (nilai default true)
    val layoutFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[IS_LIST] ?: true }

    suspend fun saveLayout(isList: Boolean) {
        context.dataStore.edit { preferences -> preferences[IS_LIST] = isList }
    }
}
