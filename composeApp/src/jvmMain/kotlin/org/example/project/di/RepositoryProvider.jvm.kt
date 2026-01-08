package org.example.project.di

import org.example.project.data.AttendanceRepositoryDesktop
import org.example.project.domain.repository.AttendanceRepository

actual fun provideAttendanceRepository(): AttendanceRepository {
    return AttendanceRepositoryDesktop(projectId = "chatchat-c2838", apiKey = "AIzaSyDNB26ewFXr7bhpLTK85Wqar1BqHUr0ObQ")
}