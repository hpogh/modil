package com.example.collegeschedule.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.dto.GroupDto

@Composable
fun GroupSelector(
    groups: List<GroupDto>,
    selectedGroup: GroupDto?,
    onGroupSelected: (GroupDto) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { expanded = true }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = selectedGroup?.name ?: "Выберите группу",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Нажмите для выбора",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        groups.forEach { group ->
            DropdownMenuItem(
                text = { Text(group.name) },
                onClick = {
                    onGroupSelected(group)
                    expanded = false
                }
            )
        }
    }
}