package org.example.project.domain.repository

import org.example.project.domain.model.Attendance
import org.example.project.domain.model.ClassRoom
import org.example.project.domain.model.Student

interface AttendanceRepository {

    // ================= CLASSES =================
    suspend fun getClasses(): List<ClassRoom>

    suspend fun addClass(classRoom: ClassRoom)

    suspend fun updateClass(classRoom: ClassRoom)

    suspend fun deleteClass(classId: String)


    // ================= STUDENTS =================
    suspend fun getStudents(classId: String): List<Student>

    suspend fun addStudent(classId: String, student: Student)

    suspend fun addStudentsFromFile(classId: String, students: List<Student>)

    suspend fun updateStudent(classId: String, student: Student)

    suspend fun deleteStudent(classId: String, studentId: String)


    // ================= ATTENDANCE =================
    suspend fun getAttendance(
        classId: String,
        date: String // yyyy-MM-dd
    ): List<Attendance>

    suspend fun markAttendance(
        classId: String,
        date: String,
        records: List<Attendance>
    )

    suspend fun updateAttendance(
        classId: String,
        date: String,
        record: Attendance
    )
}
