package org.example.project.presentation


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.di.provideAttendanceRepository
import org.example.project.domain.model.AttendanceStatus
import org.example.project.domain.repository.AttendanceRepository

@Composable
fun AttendanceScreen(
    classId: String,
    date: String,
    onBack: () -> Unit
) {
    val repo = remember { provideAttendanceRepository() }
    val vm = remember { AttendanceViewModel(repo) }

//    val vm = remember { AttendanceViewModel(provideAttendanceRepository()) }
    val attendance by vm.attendance.collectAsState()

    LaunchedEffect(Unit) { vm.loadAttendance(classId, date) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {

        Text("Attendance $date")

        LazyColumn {
            items(attendance) { a ->
                Row(
                    Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(a.studentId)
                    Button(onClick = {
                        vm.toggleAttendance(classId, date, a.studentId)
                    }) {
                        Text(if (a.status == AttendanceStatus.PRESENT) "Present" else "Absent")
                    }
                }
            }
        }

        Button(onClick = onBack) {
            Text("Back")
        }
    }
}

