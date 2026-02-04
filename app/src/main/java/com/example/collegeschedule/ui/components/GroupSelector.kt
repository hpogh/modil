package com.example.collegeschedule.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.dto.GroupDto
import com.example.collegeschedule.utils.PreferencesManager

@Composable
fun GroupSelector(
    groups: List<GroupDto>,
    selectedGroup: GroupDto?,
    onGroupSelected: (GroupDto) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    val context = LocalContext.current
    val prefs = remember { PreferencesManager(context) }
    val isFavorite = selectedGroup?.let { prefs.isFavorite(it.name) } ?: false

    val filteredGroups = if (searchText.isBlank()) {
        groups
    } else {
        groups.filter { it.name.contains(searchText, ignoreCase = true) }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Карточка выбранной группы с кнопкой избранного
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = selectedGroup?.name ?: "Выберите группу",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Нажмите для поиска",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                // Кнопка избранного
                IconButton(
                    onClick = {
                        selectedGroup?.let {
                            if (isFavorite) {
                                prefs.removeFavoriteGroup(it.name)
                            } else {
                                prefs.addFavoriteGroup(it.name)
                            }
                        }
                    },
                    enabled = selectedGroup != null
                ) {
                    Icon(
                        imageVector = if (isFavorite)
                            Icons.Default.Favorite
                        else
                            Icons.Default.FavoriteBorder,
                        contentDescription = if (isFavorite) "Удалить из избранного" else "Добавить в избранное",
                        tint = if (isFavorite) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline
                    )
                }
            }
        }

        // Dropdown меню с поиском
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            // Поле поиска
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Поиск",
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text("Поиск группы") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            Divider()

            // Список групп
            if (filteredGroups.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("Группы не найдены") },
                    onClick = { }
                )
            } else {
                filteredGroups.forEach { group ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (prefs.isFavorite(group.name)) {
                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = "В избранном",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                                Text(group.name)
                            }
                        },
                        onClick = {
                            onGroupSelected(group)
                            expanded = false
                            searchText = ""
                        }
                    )
                }
            }
        }
    }
}