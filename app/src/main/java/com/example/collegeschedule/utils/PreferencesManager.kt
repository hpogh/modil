package com.example.collegeschedule.utils

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferencesManager(context: Context) {
    private val prefs = context.getSharedPreferences("college_schedule", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Сохранить список избранных групп
    fun saveFavoriteGroups(groups: List<String>) {
        val json = gson.toJson(groups)
        prefs.edit {
            putString("favorite_groups", json)
        }
    }

    // Получить список избранных групп
    fun getFavoriteGroups(): List<String> {
        val json = prefs.getString("favorite_groups", "[]") ?: "[]"
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    // Добавить группу в избранное
    fun addFavoriteGroup(groupName: String) {
        val current = getFavoriteGroups().toMutableList()
        if (!current.contains(groupName)) {
            current.add(groupName)
            saveFavoriteGroups(current)
        }
    }

    // Удалить группу из избранного
    fun removeFavoriteGroup(groupName: String) {
        val current = getFavoriteGroups().toMutableList()
        current.remove(groupName)
        saveFavoriteGroups(current)
    }

    // Проверить, есть ли группа в избранном
    fun isFavorite(groupName: String): Boolean {
        return getFavoriteGroups().contains(groupName)
    }
}