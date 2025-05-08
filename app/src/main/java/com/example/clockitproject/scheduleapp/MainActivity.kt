package com.example.clockitproject.scheduleapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ScheduleApp()
            }
        }
    }
}

// ───────────────────────────────────────────────────
// Root of your Compose UI
@Composable
fun ScheduleApp(
    taskViewModel: TaskViewModel = viewModel()
) {
    // 1) Local state to track whether the dialog is showing
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar                     = { MonthNavigator() },
        bottomBar                  = { BottomNavBar() },
        floatingActionButton       = {
            // 2) FAB now toggles `showDialog`
            AddTaskFab(onClick = { showDialog = true })
        },
        floatingActionButtonPosition = androidx.compose.material3.FabPosition.End
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            WeekDaysRow()
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Today's Tasks:",
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            TaskList(
                tasks = taskViewModel.tasks,
                modifier = Modifier.weight(1f)
            )
        }
    }

    // 3) Show the dialog when requested
    if (showDialog) {
        AddTaskDialog(
            onAdd = { newTask ->
                taskViewModel.addTask(newTask)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleAppPreview() {
    ScheduleApp()
}

// ───────────────────────────────────────────────────
// Month navigator with back/forward arrows
@Composable
fun MonthNavigator(
    month: String = "February",
    year: Int = 2025,
    onPrevious: () -> Unit = {},
    onNext: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment   = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onPrevious) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Previous Month"
            )
        }
        Surface(shape = RoundedCornerShape(16.dp), tonalElevation = 4.dp) {
            Text(
                text = "$month  $year",
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                fontSize = 18.sp
            )
        }
        IconButton(onClick = onNext) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Next Month")
        }
    }
}


// ───────────────────────────────────────────────────
// A single day column in the weekday row
data class DayOfWeekItem(val label: String, val date: Int)

@Composable
fun WeekDaysRow(
    days: List<DayOfWeekItem> = listOf(
        DayOfWeekItem("Sun", 17),
        DayOfWeekItem("Mon", 18),
        DayOfWeekItem("Tue", 19),
        DayOfWeekItem("Wed", 20),
        DayOfWeekItem("Thu", 21),
        DayOfWeekItem("Fri", 22),
        DayOfWeekItem("Sat", 23),
    ),
    onDayClick: (DayOfWeekItem) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEach { day ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onDayClick(day) }
            ) {
                Text(day.label, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(day.date.toString(), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

// ───────────────────────────────────────────────────
// One task card
@Composable
fun TaskItem(task: Task, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(task.location, style = MaterialTheme.typography.bodySmall)
            }
            Text(
                text = "${task.startTime} – ${task.endTime}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

// ───────────────────────────────────────────────────
// The scrollable list of TaskItems
@Composable
fun TaskList(tasks: List<Task>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(tasks) { t ->
            TaskItem(task = t)
        }
    }
}

// ───────────────────────────────────────────────────
// Bottom navigation bar
@Composable
fun BottomNavBar(
    selectedIndex: Int = 0,
    onItemSelected: (Int) -> Unit = {}
) {
    NavigationBar {
        val items = listOf(
            Icons.Filled.CalendarToday to "Calendar",
            Icons.Filled.Visibility    to "View",
            Icons.Filled.Person        to "Profile"
        )
        items.forEachIndexed { idx, (icon, desc) ->
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = desc) },
                selected = (selectedIndex == idx),
                onClick = { onItemSelected(idx) }
            )
        }
    }
}

@Composable
fun AddTaskDialog(
    onAdd: (Task) -> Unit,
    onDismiss: () -> Unit
) {
    // local state for the four fields
    var title by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Task") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    singleLine = true,
                    modifier = Modifier.padding(top = 8.dp)
                )
                OutlinedTextField(
                    value = startTime,
                    onValueChange = { startTime = it },
                    label = { Text("Start Time") },
                    singleLine = true,
                    modifier = Modifier.padding(top = 8.dp)
                )
                OutlinedTextField(
                    value = endTime,
                    onValueChange = { endTime = it },
                    label = { Text("End Time") },
                    singleLine = true,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // create and add the task, then close dialog
                    onAdd(Task(title, location, startTime, endTime))
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// ───────────────────────────────────────────────────
// Floating Action Button to add tasks
@Composable
fun AddTaskFab(onClick: () -> Unit = {}) {
    FloatingActionButton(onClick = onClick) {
        Icon(Icons.Filled.Add, contentDescription = "Add Task")
    }
}

