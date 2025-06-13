package com.tiyasinsania0090.wishlystack.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tiyasinsania0090.wishlystack.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore1: DataStore<Preferences> by preferencesDataStore(name = "wishlystack_settings")

class SettingDataStore(private val context: Context) {

    companion object {
        private val IS_LIST = booleanPreferencesKey("is_list")

        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val USER_PICTURE_URL_KEY = stringPreferencesKey("user_picture_url")
    }

    val layoutFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[IS_LIST] ?: true }

    suspend fun saveLayout(isList: Boolean) {
        context.dataStore.edit { preferences -> preferences[IS_LIST] = isList }
    }

    val userFlow: Flow<User> = context.dataStore.data.map { preferences ->
        User(
            name = preferences[USER_NAME_KEY] ?: "",
            email = preferences[USER_EMAIL_KEY] ?: "",
            pictureUrl = preferences[USER_PICTURE_URL_KEY] ?: ""
        )
    }

    suspend fun saveUser(user: User) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = user.name
            preferences[USER_EMAIL_KEY] = user.email
            preferences[USER_PICTURE_URL_KEY] = user.pictureUrl
        }
    }
}