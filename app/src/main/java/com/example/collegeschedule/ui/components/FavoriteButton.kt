package com.example.collegeschedule.ui.components

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import com.example.collegeschedule.utils.PreferencesManager

@Composable
fun FavoriteButton(
    groupName: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isFavorite by remember { mutableStateOf(false) }

    IconButton(
        onClick = {
            PreferencesManager(context).saveFavoriteGroup(groupName)
            isFavorite = true
        },
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isFavorite)
                Icons.Default.Favorite
            else
                Icons.Default.FavoriteBorder,
            contentDescription = "Добавить в избранное"
        )
    }
}