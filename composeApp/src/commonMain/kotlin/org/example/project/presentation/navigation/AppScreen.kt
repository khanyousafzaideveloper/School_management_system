package org.example.project.presentation.navigation


sealed class AppScreen {
    data object Classes : AppScreen()
    data class Students(val classId: String, val className: String) : AppScreen()
    data class Attendance(val classId: String, val date: String) : AppScreen()
}
