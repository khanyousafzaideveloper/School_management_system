package org.example.project.presentation


import androidx.compose.runtime.*
import org.example.project.presentation.navigation.AppScreen

@Composable
fun SchoolManagementSystem() {
    var screen by remember { mutableStateOf<AppScreen>(AppScreen.Classes) }

    when (val s = screen) {
        is AppScreen.Classes -> ClassesScreen(
            onOpenClass = { id, name ->
                screen = AppScreen.Students(id, name)
            }
        )

        is AppScreen.Students -> StudentsScreen(
            classId = s.classId,
            className = s.className,
            onBack = { screen = AppScreen.Classes },
            onTakeAttendance = { date ->
                screen = AppScreen.Attendance(s.classId, date)
            }
        )

        is AppScreen.Attendance -> AttendanceScreen(
            classId = s.classId,
            date = s.date,
            onBack = {
                screen = AppScreen.Students(s.classId, "Back")
            }
        )
    }
}
