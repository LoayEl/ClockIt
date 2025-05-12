package com.example.clockitproject.scheduleapp

// Compose + ViewModel
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel

// Foundation & Layout
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape

// Material3
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.FabPosition

// Icons
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward

// UI
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Date APIs
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale

/**
 * The “Calendar” screen: month header, 7-day strip, tasks for
 * the selected day, plus a FAB + Add-Task dialog.
 */
@Composable
fun ScheduleScreen(
    taskViewModel: TaskViewModel       // ← injected from AppNavHost
) {
    // 1) Dialog visibility
    var showDialog by remember { mutableStateOf(false) }

    // 2) Month header state
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    // 3) The Sunday that begins the visible week
    var weekStart by remember {
        mutableStateOf(
            LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
        )
    }

    // 4) The date tapped by the user
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    // 5) Build the 7-day window
    val weekDays = (0..6).map { offset ->
        val d = weekStart.plusDays(offset.toLong())
        DayOfWeekItem(
            label    = d.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
            date     = d.dayOfMonth,
            fullDate = d
        )
    }

    Scaffold(
        topBar = {
            MonthNavigator(
                month      = currentMonth.month
                    .getDisplayName(TextStyle.FULL, Locale.getDefault())
                    .replaceFirstChar { it.uppercaseChar() },
                year       = currentMonth.year,
                onPrevious = { currentMonth = currentMonth.minusMonths(1) },
                onNext     = { currentMonth = currentMonth.plusMonths(1) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Task")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Spacer(Modifier.height(8.dp))

            WeekStrip(
                days = weekDays,
                selected = selectedDate,
                onDaySelected = { selectedDate = it.fullDate },
                onPrevWeek = { weekStart = weekStart.minusWeeks(1) },
                onNextWeek = { weekStart = weekStart.plusWeeks(1) }
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Today's Tasks:",
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(Modifier.height(8.dp))

            TaskList(
                tasks = taskViewModel.tasks.filter { it.date == selectedDate },
                modifier = Modifier.weight(1f)
            )
        }
    }

    if (showDialog) {
        AddTaskDialog(
            defaultDate = selectedDate,
            onAdd = {
                taskViewModel.addTask(it)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleScreenPreview() {
    // For preview only: instantiate your ViewModel directly.
    ScheduleScreen(taskViewModel = TaskViewModel())
}


// ───────────────────────────────────────────────────
// Data for each day cell
data class DayOfWeekItem(
    val label: String,
    val date: Int,
    val fullDate: LocalDate
)

// ───────────────────────────────────────────────────
// The horizontal week strip with prev/next arrows
@Composable
fun WeekStrip(
    days: List<DayOfWeekItem>,
    selected: LocalDate,
    onDaySelected: (DayOfWeekItem) -> Unit,
    onPrevWeek: () -> Unit,
    onNextWeek: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment   = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onPrevWeek) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Previous Week")
        }

        Row(
            Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            days.forEach { day ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onDaySelected(day) }
                ) {
                    Text(day.label, style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        day.date.toString(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = if (day.fullDate == selected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            }
        }

        IconButton(onClick = onNextWeek) {
            Icon(Icons.Filled.ArrowForward, contentDescription = "Next Week")
        }
    }
}

// ───────────────────────────────────────────────────
// Month picker with back/forward arrows
@Composable
fun MonthNavigator(
    month: String,
    year: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment   = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onPrevious) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Previous Month")
        }
        Surface(shape = RoundedCornerShape(16.dp), tonalElevation = 4.dp) {
            Text(
                text = "$month $year",
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                fontSize = 18.sp
            )
        }
        IconButton(onClick = onNext) {
            Icon(Icons.Filled.ArrowForward, contentDescription = "Next Month")
        }
    }
}

// ───────────────────────────────────────────────────
// One Task card
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


@Composable
fun TaskList(tasks: List<Task>, modifier: Modifier = Modifier) {
    LazyColumn(modifier) {
        items(tasks) { TaskItem(it) }
    }
}

// ───────────────────────────────────────────────────
// Add-Task dialog
@Composable
fun AddTaskDialog(
    defaultDate: LocalDate,
    onAdd: (Task) -> Unit,
    onDismiss: () -> Unit
) {
    // dialog state
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
            TextButton(onClick = {
                onAdd(Task(title, location, startTime, endTime, defaultDate))
                onDismiss()
            }) {
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
