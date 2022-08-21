package com.learn.ktornoteapp.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.learn.ktornoteapp.utils.Constant.JWT_TOKEN_KEY
import kotlinx.coroutines.flow.first

class SessionManager(val context: Context) {
    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(
        "session_manager"
    )

    suspend fun saveJwtToken(token: String) {
        val jwtTokenKey = stringPreferencesKey(JWT_TOKEN_KEY)
        context.datastore.edit { preferences ->
            preferences[jwtTokenKey] = token
        }
    }

    suspend fun getJwtToken(): String? {
        val jwtTokenKey = stringPreferencesKey(JWT_TOKEN_KEY)
        val preferences = context.datastore.data.first()
        return preferences[jwtTokenKey]
    }
}