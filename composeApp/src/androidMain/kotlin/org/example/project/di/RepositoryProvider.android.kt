package org.example.project.di

import org.example.project.data.AttendanceRepositoryAndroid
import org.example.project.domain.repository.AttendanceRepository

actual fun provideAttendanceRepository(): AttendanceRepository {
    return AttendanceRepositoryAndroid()
}