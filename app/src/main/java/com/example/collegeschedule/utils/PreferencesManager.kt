package com.example.collegeschedule.utils

import android.content.Context
import androidx.core.content.edit

class PreferencesManager(context: Context) {
    private val prefs = context.getSharedPreferences("college_schedule", Context.MODE_PRIVATE)

    fun saveFavoriteGroup(groupName: String) {
        prefs.edit {
            putString("favorite_group", groupName)
        }
    }

    fun getFavoriteGroup(): String? {
        return prefs.getString("favorite_group", null)
    }
}