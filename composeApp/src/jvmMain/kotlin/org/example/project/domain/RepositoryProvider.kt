package org.example.project.domain


import org.example.project.data.AttendanceRepositoryDesktop
import org.example.project.domain.repository.AttendanceRepository

private const val PROJECT_ID = "chatchat-c2838"
private const val API_KEY = "AIzaSyDNB26ewFXr7bhpLTK85Wqar1BqHUr0ObQ"

actual fun provideAttendanceRepository(): AttendanceRepository =
    AttendanceRepositoryDesktop(PROJECT_ID, API_KEY)
