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

// Ganti nama preference jika Anda mau, atau biarkan sama
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "wishlystack_settings")

class SettingDataStore(private val context: Context) {

    // Kunci untuk menyimpan semua preferensi
    companion object {
        // Kunci yang sudah ada
        private val IS_LIST = booleanPreferencesKey("is_list")

        // PERUBAHAN 1: Tambahkan kunci untuk data user
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val USER_PICTURE_URL_KEY = stringPreferencesKey("user_picture_url")
    }

    // --- Kode untuk Layout (sudah ada, tidak diubah) ---
    val layoutFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[IS_LIST] ?: true }

    suspend fun saveLayout(isList: Boolean) {
        context.dataStore.edit { preferences -> preferences[IS_LIST] = isList }
    }


    // =======================================================
    // PERUBAHAN 2: Tambahkan logika untuk data User
    // =======================================================

    /**
     * Flow untuk menyediakan data User secara real-time.
     * Ia membaca setiap preferensi (nama, email, foto) dan menggabungkannya
     * menjadi sebuah objek User.
     */
    val userFlow: Flow<User> = context.dataStore.data.map { preferences ->
        User(
            name = preferences[USER_NAME_KEY] ?: "",
            // Diberi nilai default sementara agar tidak error sebelum ada fitur login
            email = preferences[USER_EMAIL_KEY] ?: "tiyasinsania@gmail.com",
            pictureUrl = preferences[USER_PICTURE_URL_KEY] ?: ""
        )
    }

    /**
     * Fungsi untuk menyimpan data user ke DataStore.
     * Akan sangat berguna saat Anda membuat fitur login nanti.
     */
    suspend fun saveUser(user: User) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = user.name
            preferences[USER_EMAIL_KEY] = user.email
            preferences[USER_PICTURE_URL_KEY] = user.pictureUrl
        }
    }
}