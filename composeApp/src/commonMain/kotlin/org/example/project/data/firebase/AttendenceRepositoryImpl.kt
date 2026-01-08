package org.example.project.data.firebase

import kotlinx.serialization.Serializable
import org.example.project.domain.model.Attendance
import org.example.project.domain.model.ClassRoom
import org.example.project.domain.model.Student
import org.example.project.domain.repository.AttendanceRepository
//
//class AttendanceRepositoryAndroid(
//    private val firestore: FirebaseFirestore
//) : AttendanceRepository {
//
//    override suspend fun addClass(classRoom: ClassRoom) {
//        val doc = firestore.collection("classes").document()
//        doc.set(classRoom.copy(id = doc.id)).await()
//    }
//
//    override suspend fun getClasses(): List<ClassRoom> {
//        return firestore.collection("classes")
//            .get().await()
//            .documents.mapNotNull {
//                it.toObject(ClassRoom::class.java)
//            }
//    }
//
//
//
//    override suspend fun addStudent(student: Student) {
//        val doc = firestore
//            .collection("classes")
//            .document(student.classId)
//            .collection("students")
//            .document()
//
//        doc.set(student.copy(id = doc.id)).await()
//    }
//
//    override suspend fun getStudents(classId: String): List<Student> {
//        return firestore.collection("classes")
//            .document(classId)
//            .collection("students")
//            .get().await()
//            .documents.mapNotNull {
//                it.toObject(Student::class.java)
//            }
//    }
//
//
//    override suspend fun saveAttendance(
//        classId: String,
//        date: String,
//        attendance: List<Attendance>
//    ) {
//        val ref = firestore.collection("classes")
//            .document(classId)
//            .collection("attendance")
//            .document(date)
//
//        attendance.forEach {
//            ref.collection("students")
//                .document(it.studentId)
//                .set(it)
//        }
//    }
//
//    override suspend fun getAttendance(
//        classId: String,
//        date: String
//    ): List<Attendance> {
//        return firestore.collection("classes")
//            .document(classId)
//            .collection("attendance")
//            .document(date)
//            .collection("students")
//            .get().await()
//            .documents.mapNotNull {
//                it.toObject(Attendance::class.java)
//            }
//    }
//
//}
//
//fun firestoreDoc(builder: FirestoreBuilder.() -> Unit): Map<String, Any> {
//    return mapOf(
//        "fields" to FirestoreBuilder().apply(builder).build()
//    )
//}
//
//class FirestoreBuilder {
//    private val fields = mutableMapOf<String, Any>()
//
//    fun string(key: String, value: String) {
//        fields[key] = mapOf("stringValue" to value)
//    }
//
//    fun boolean(key: String, value: Boolean) {
//        fields[key] = mapOf("booleanValue" to value)
//    }
//
//    fun build() = fields
//}


@Serializable
data class FirestoreListResponse(
    val documents: List<FirestoreDocument>? = emptyList()
)

@Serializable
data class FirestoreDocument(
    val name: String,
    val fields: FirestoreFields
)

@Serializable
data class FirestoreFields(
    val name: FirestoreValue
)

@Serializable
data class FirestoreValue(
    val stringValue: String
)
