package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.dto.GroupDto
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.network.RetrofitInstance
import com.example.collegeschedule.ui.components.GroupSelector
import com.example.collegeschedule.utils.getWeekDateRange
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen() {
    var groups by remember { mutableStateOf<List<GroupDto>>(emptyList()) }
    var selectedGroup by remember { mutableStateOf<GroupDto?>(null) }
    var schedule by remember { mutableStateOf<List<ScheduleByDateDto>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        // Загружаем список групп
        try {
            groups = RetrofitInstance.groupsApi.getAllGroups()
            if (groups.isNotEmpty()) {
                selectedGroup = groups[0]  // Выбираем первую группу по умолчанию
            }
        } catch (e: Exception) {
            error = "Ошибка загрузки групп: ${e.message}"
        }
        loading = false
    }

    // Загружаем расписание при выборе группы
    LaunchedEffect(selectedGroup) {
        selectedGroup?.let { group ->
            try {
                val (start, end) = getWeekDateRange()
                schedule = RetrofitInstance.api.getSchedule(
                    group.name,
                    start,
                    end
                )
                error = null
            } catch (e: Exception) {
                error = "Ошибка загрузки расписания: ${e.message}"
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Расписание") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Выбор группы
            GroupSelector(
                groups = groups,
                selectedGroup = selectedGroup,
                onGroupSelected = { group ->
                    selectedGroup = group
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Расписание
            when {
                loading -> CircularProgressIndicator()
                error != null -> Text("Ошибка: $error")
                else -> ScheduleList(schedule)
            }
        }
    }
}