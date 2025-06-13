package com.tiyasinsania0090.wishlystack.util

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tiyasinsania0090.wishlystack.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserDataStore (private val context: Context){
    companion object {
        val USER_NAME = stringPreferencesKey("name")
        val USER_EMAIL = stringPreferencesKey("email")
        val USER_PHOTO = stringPreferencesKey("photoUrl")
    }

    fun getUserFlow(): Flow<User> {
        return context.dataStore.data.map { preferences ->
            User(
                name = preferences[USER_NAME] ?: "",
                email = preferences[USER_EMAIL] ?: "",
                pictureUrl = preferences[USER_PHOTO] ?: ""
            )
        }
    }


    suspend fun saveData(user: User) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = user.name
            preferences[USER_EMAIL] = user.email
            preferences[USER_PHOTO] = user.pictureUrl
        }
        Log.d("DATASTORE", "Sukses simpan: ${user.name} - ${user.email}")
    }

}