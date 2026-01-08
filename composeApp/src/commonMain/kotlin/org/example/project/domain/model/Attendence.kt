package org.example.project.domain.model

data class Attendance(
    val id: String = "",
    val studentId: String = "",
    val date: String, // yyyy-MM-dd
    val status: AttendanceStatus
)

enum class AttendanceStatus {
    PRESENT, ABSENT
}
