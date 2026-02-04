package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.collegeschedule.data.dto.LessonDto
import com.example.collegeschedule.data.dto.ScheduleByDateDto

@Composable
fun ScheduleList(schedule: List<ScheduleByDateDto>) {
    if (schedule.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "Нет занятий",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Нет занятий на эту неделю",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(schedule) { daySchedule ->
                DayScheduleCard(daySchedule)
            }
        }
    }
}

@Composable
fun DayScheduleCard(daySchedule: ScheduleByDateDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Заголовок дня
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = daySchedule.weekday,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = daySchedule.lessonDate,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Icon(
                    imageVector = getDayIcon(daySchedule.weekday),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider()
            Spacer(modifier = Modifier.height(12.dp))

            // Занятия
            if (daySchedule.lessons.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Выходной",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    daySchedule.lessons.forEach { lesson ->
                        LessonCard(lesson)
                    }
                }
            }
        }
    }
}

@Composable
fun LessonCard(lesson: LessonDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
        ) {
            // Время
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(80.dp)
            ) {
                Badge(
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = lesson.lessonNumber.toString(),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = lesson.time,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Информация о занятии
            Column(
                modifier = Modifier.weight(1f)
            ) {
                lesson.groupParts.forEach { (part, details) ->
                    if (details != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            // Иконка типа занятия
                            Icon(
                                imageVector = getLessonTypeIcon(details.subject),
                                contentDescription = null,
                                tint = getBuildingColor(details.building),
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Column {
                                Text(
                                    text = details.subject,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )

                                Text(
                                    text = "${details.teacher} • ${details.classroom}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = getBuildingColor(details.building),
                                        modifier = Modifier.size(14.dp)
                                    )

                                    Spacer(modifier = Modifier.width(4.dp))

                                    Text(
                                        text = "${details.building} (${details.address})",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = getBuildingColor(details.building)
                                    )
                                }
                            }
                        }

                        if (part.name != "FULL") {
                            Badge(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Text(
                                    text = when (part.name) {
                                        "SUB1" -> "Подгруппа 1"
                                        "SUB2" -> "Подгруппа 2"
                                        else -> part.name
                                    },
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Вспомогательные функции для дизайна
private fun getDayIcon(weekday: String): ImageVector {
    return when (weekday.lowercase()) {
        "понедельник" -> Icons.Default.Monday
        "вторник" -> Icons.Default.Tuesday
        "среда" -> Icons.Default.Wednesday
        "четверг" -> Icons.Default.Thursday
        "пятница" -> Icons.Default.Friday
        "суббота" -> Icons.Default.Saturday
        else -> Icons.Default.CalendarToday
    }
}

private fun getLessonTypeIcon(subject: String): ImageVector {
    return when {
        subject.contains("лек", ignoreCase = true) -> Icons.Default.MenuBook
        subject.contains("практ", ignoreCase = true) -> Icons.Default.Group
        subject.contains("лаб", ignoreCase = true) -> Icons.Default.Science
        else -> Icons.Default.School
    }
}

private fun getBuildingColor(building: String): Color {
    return when {
        building.contains("глав", ignoreCase = true) -> Color(0xFF4CAF50)  // Зеленый
        building.contains("корпус а", ignoreCase = true) -> Color(0xFF2196F3)  // Синий
        building.contains("корпус б", ignoreCase = true) -> Color(0xFF9C27B0)  // Фиолетовый
        building.contains("спорт", ignoreCase = true) -> Color(0xFFFF9800)  // Оранжевый
        else -> MaterialTheme.colorScheme.primary
    }
}