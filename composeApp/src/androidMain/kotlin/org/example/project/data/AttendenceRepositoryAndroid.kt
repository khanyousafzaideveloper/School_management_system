package org.example.project.data


import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.example.project.domain.model.Attendance
import org.example.project.domain.model.AttendanceStatus
import org.example.project.domain.model.ClassRoom
import org.example.project.domain.model.Student
import org.example.project.domain.repository.AttendanceRepository

class AttendanceRepositoryAndroid : AttendanceRepository {

    private val db = FirebaseFirestore.getInstance()

    override suspend fun getClasses(): List<ClassRoom> {
        return db.collection("classes")
            .get()
            .await()
            .documents
            .map {
                ClassRoom(
                    id = it.id,
                    name = it.getString("name") ?: ""
                )
            }
    }

    override suspend fun addClass(classRoom: ClassRoom) {
        db.collection("classes")
            .add(mapOf("name" to classRoom.name))
            .await()
    }

    override suspend fun updateClass(classRoom: ClassRoom) {
        db.collection("classes")
            .document(classRoom.id)
            .update("name", classRoom.name)
            .await()
    }

    override suspend fun deleteClass(classId: String) {
        db.collection("classes")
            .document(classId)
            .delete()
            .await()
    }

    override suspend fun getStudents(classId: String): List<Student> {
        return db.collection("classes")
            .document(classId)
            .collection("students")
            .get()
            .await()
            .documents
            .map {
                Student(
                    id = it.id,
                    name = it.getString("name") ?: ""
                )
            }
    }

    override suspend fun addStudent(classId: String, student: Student) {
        db.collection("classes")
            .document(classId)
            .collection("students")
            .add(mapOf("name" to student.name))
            .await()
    }

    override suspend fun addStudentsFromFile(classId: String, students: List<Student>) {
        val batch = db.batch()
        val ref = db.collection("classes")
            .document(classId)
            .collection("students")

        students.forEach {
            batch.set(ref.document(), mapOf("name" to it.name))
        }

        batch.commit().await()
    }

    override suspend fun updateStudent(classId: String, student: Student) {
        db.collection("classes")
            .document(classId)
            .collection("students")
            .document(student.id)
            .update("name", student.name)
            .await()
    }

    override suspend fun deleteStudent(classId: String, studentId: String) {
        db.collection("classes")
            .document(classId)
            .collection("students")
            .document(studentId)
            .delete()
            .await()
    }

    override suspend fun getAttendance(classId: String, date: String): List<Attendance> {
        return db.collection("attendance")
            .document(classId)
            .collection(date)
            .get()
            .await()
            .documents
            .map {
                Attendance(
                    id = it.id,
                    studentId = it.getString("studentId") ?: "",
                    date = date,
                    status = AttendanceStatus.valueOf(it.getString("status") ?: "ABSENT")
                )
            }
    }

    override suspend fun markAttendance(
        classId: String,
        date: String,
        records: List<Attendance>
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun updateAttendance(
        classId: String,
        date: String,
        record: Attendance
    ) {
        TODO("Not yet implemented")
    }

}
