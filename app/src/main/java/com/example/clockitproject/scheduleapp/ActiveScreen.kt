package com.example.clockitproject.scheduleapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

/**
 * Shows an “Active” header and then a full list of all tasks,
 * in the natural order stored in the ViewModel.
 */
@Composable
fun ActiveScreen(taskViewModel: TaskViewModel = viewModel()) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Header band
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Active",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Simply show all tasks
        TaskList(
            tasks = taskViewModel.tasks,
            modifier = Modifier.padding(bottom = 64.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ActiveScreenPreview() {
    ActiveScreen()
}
