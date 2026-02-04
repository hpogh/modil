package com.example.collegeschedule.ui.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.network.RetrofitInstance
import com.example.collegeschedule.ui.schedule.ScheduleList
import com.example.collegeschedule.utils.PreferencesManager
import com.example.collegeschedule.utils.getWeekDateRange
import kotlinx.coroutines.launch

@Composable
fun FavoritesScreen(
    onGroupSelected: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val prefs = remember { PreferencesManager(context) }
    var favoriteGroups by remember { mutableStateOf(prefs.getFavoriteGroups()) }
    var selectedGroup by remember { mutableStateOf<String?>(null) }
    var schedule by remember { mutableStateOf<List<ScheduleByDateDto>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(selectedGroup) {
        selectedGroup?.let { groupName ->
            loading = true
            try {
                val (start, end) = getWeekDateRange()
                schedule = RetrofitInstance.api.getSchedule(groupName, start, end)
            } catch (e: Exception) {
                // Игнорируем ошибки для демо
            } finally {
                loading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Список избранных групп
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Избранные группы (${favoriteGroups.size})",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (favoriteGroups.isEmpty()) {
                    Text(
                        text = "Добавьте группы в избранное на главном экране",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                } else {
                    LazyColumn {
                        items(favoriteGroups) { groupName ->
                            FavoriteGroupItem(
                                groupName = groupName,
                                isSelected = selectedGroup == groupName,
                                onSelect = {
                                    selectedGroup = groupName
                                    onGroupSelected(groupName)
                                },
                                onRemove = {
                                    prefs.removeFavoriteGroup(groupName)
                                    favoriteGroups = prefs.getFavoriteGroups()
                                    if (selectedGroup == groupName) {
                                        selectedGroup = null
                                        schedule = emptyList()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Расписание выбранной группы
        if (selectedGroup != null) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Расписание: $selectedGroup",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (loading) {
                        CircularProgressIndicator()
                    } else {
                        ScheduleList(schedule)
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteGroupItem(
    groupName: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onSelect() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Избранное",
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = groupName,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Удалить",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}