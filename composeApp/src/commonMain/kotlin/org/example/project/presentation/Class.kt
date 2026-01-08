package org.example.project.presentation


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.di.provideAttendanceRepository
import org.example.project.domain.model.ClassRoom

@Composable
fun ClassesScreen(
    onOpenClass: (String, String) -> Unit
) {
    val repo = remember { provideAttendanceRepository() }
    val vm = remember { AttendanceViewModel(repo) }

    val classes by vm.classes.collectAsState()

    var newClass by remember { mutableStateOf("") }

    LaunchedEffect(Unit) { vm.loadClasses() }

    Column(Modifier.fillMaxSize().padding(16.dp)) {

        Text("Classes", style = MaterialTheme.typography.headlineSmall)

        Row {
            OutlinedTextField(
                value = newClass,
                onValueChange = { newClass = it },
                label = { Text("New Class") }
            )

            Button(
                onClick = {
                    if (newClass.isNotBlank()) vm.addClass(newClass)
                    newClass = ""
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Add")
            }
        }

        Spacer(Modifier.height(12.dp))

        LazyColumn {
            items(classes) { c: ClassRoom ->
                Button(
                    onClick = { onOpenClass(c.id, c.name) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                ) {
                    Text(c.name)
                }
            }
        }
    }
}
