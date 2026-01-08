package org.example.project.domain


import org.example.project.data.AttendanceRepositoryAndroid
import org.example.project.domain.repository.AttendanceRepository

actual fun provideAttendanceRepository(): AttendanceRepository =
    AttendanceRepositoryAndroid()
