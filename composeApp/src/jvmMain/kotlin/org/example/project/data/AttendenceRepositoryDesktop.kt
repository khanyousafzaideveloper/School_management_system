package org.example.project.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.patch
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.example.project.data.firebase.FirestoreListResponse
import org.example.project.domain.model.Attendance
import org.example.project.domain.model.ClassRoom
import org.example.project.domain.model.Student
import org.example.project.domain.repository.AttendanceRepository


class AttendanceRepositoryDesktop(
    private val projectId: String,
    private val apiKey: String
) : AttendanceRepository {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true   // 🔥 THIS FIXES IT
                    prettyPrint = false
                    isLenient = true
                }
            )
        }
    }

    private val baseUrl =
        "https://firestore.googleapis.com/v1/projects/$projectId/databases/(default)/documents"

    override suspend fun getClasses(): List<ClassRoom> {
        println("🔥 Desktop → Fetching classes")
        println("🌐 URL = $baseUrl/classes?key=$apiKey")
        println("📌 projectId = $projectId")

        return try {
            val response: HttpResponse = client.get("$baseUrl/classes") {
                parameter("key", apiKey)
            }

            println("✅ HTTP Status = ${response.status}")
            println("📨 Raw Response = ${response.bodyAsText()}")

            if (response.status != HttpStatusCode.OK) {
                println("❌ Failed to load classes")
                return emptyList()
            }

            val body = response.body<FirestoreListResponse>()

            if (body.documents == null) {
                println("⚠️ No documents returned")
                return emptyList()
            }

            val list = body.documents.map {
                println("📗 Found class document → ${it.name}")
                ClassRoom(
                    id = it.name.substringAfterLast("/"),
                    name = it.fields.name.stringValue
                )
            }

            println("🎯 Total classes loaded = ${list.size}")
            list
        } catch (e: Exception) {
            println("💥 ERROR while fetching classes from Firestore REST")
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun addClass(classRoom: ClassRoom) {
        println("🔥 Desktop → Adding Class: ${classRoom.name}")

        try {
            val response: HttpResponse = client.post("$baseUrl/classes") {
                parameter("key", apiKey)
                setBody(
                    firestoreDoc {
                        string("name", classRoom.name)
                    }
                )
            }

            println("✅ HTTP Status = ${response.status}")
            println("📨 Raw Response = ${response.bodyAsText()}")

        } catch (e: Exception) {
            println("💥 ERROR adding class to Firestore")
            e.printStackTrace()
        }
    }

    // ------- Not implemented yet -------
    override suspend fun getStudents(classId: String): List<Student> {
        println("🔥 Desktop → Fetching students for class = $classId")

        return try {
            val url = "$baseUrl/classes/$classId/students"
            println("🌐 URL = $url?key=$apiKey")

            val response = client.get(url) {
                parameter("key", apiKey)
            }.body<FirestoreResponse>()

            println("📨 Students Raw Response = $response")

            response.documents.mapNotNull { doc ->
                val f = doc.fields ?: return@mapNotNull null

                Student(
                    id = doc.name.substringAfterLast("/"),
                    name = f.name?.stringValue ?: "",
                    rollNo = f.roll?.stringValue?.toInt() ?: 0
                )
            }

        } catch (e: Exception) {
            println("💥 ERROR fetching students")
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun addStudent(classId: String, student: Student) {
        println("🔥 Desktop → Adding Student: ${student.name} in class $classId")

        try {
            val response = client.post("$baseUrl/classes/$classId/students") {
                parameter("key", apiKey)
                setBody(
                    firestoreDoc {
                        string("name", student.name)
                        string("roll", student.rollNo.toString())
                    }
                )
            }

            println("✅ HTTP Status = ${response.status}")
            println("📨 Raw Response = ${response.bodyAsText()}")

        } catch (e: Exception) {
            println("💥 ERROR adding student to Firestore")
            e.printStackTrace()
        }
    }
    override suspend fun addStudentsFromFile(classId: String, students: List<Student>) {
        println("🔥 Desktop → Bulk Add Students (${students.size})")

        students.forEach { addStudent(classId, it) }
    }
    override suspend fun updateStudent(classId: String, student: Student) {
        println("🔥 Desktop → Updating Student: ${student.id}")

        val docPath = "$baseUrl/classes/$classId/students/${student.id}"

        try {
            val response = client.patch(docPath) {
                parameter("key", apiKey)
                setBody(
                    firestoreDoc {
                        string("name", student.name)
                        string("roll", student.rollNo.toString())
                    }
                )
            }

            println("✅ HTTP Status = ${response.status}")
            println("📨 Raw Response = ${response.bodyAsText()}")

        } catch (e: Exception) {
            println("💥 ERROR updating student")
            e.printStackTrace()
        }
    }
    override suspend fun deleteStudent(classId: String, studentId: String) {}
    override suspend fun getAttendance(classId: String, date: String): List<Attendance> = emptyList()
    override suspend fun markAttendance(classId: String, date: String, records: List<Attendance>) {}
    override suspend fun updateAttendance(classId: String, date: String, record: Attendance) {}
    override suspend fun updateClass(classRoom: ClassRoom) {}
    override suspend fun deleteClass(classId: String) {}
}


// ---------------- Helper JSON Builder ----------------

fun firestoreDoc(builder: FirestoreFieldsBuilder.() -> Unit): JsonObject {
    val fieldsBuilder = FirestoreFieldsBuilder().apply(builder)

    return buildJsonObject {
        put("fields", JsonObject(fieldsBuilder.fields))
    }
}

class FirestoreFieldsBuilder {
    internal val fields = mutableMapOf<String, JsonElement>()

    fun string(name: String, value: String) {
        fields[name] = buildJsonObject {
            put("stringValue", value)
        }
    }

    fun boolean(name: String, value: Boolean) {
        fields[name] = buildJsonObject {
            put("booleanValue", value)
        }
    }

    fun number(name: String, value: Number) {
        fields[name] = buildJsonObject {
            put("integerValue", value.toString())
        }
    }
}


@Serializable
data class FirestoreResponse(
    val documents: List<FirestoreDocument> = emptyList()
)

@Serializable
data class FirestoreDocument(
    val name: String,
    val fields: FirestoreFields? = null
)

@Serializable
data class FirestoreFields(
    val name: FirestoreStringValue? = null,
    val roll: FirestoreStringValue? = null
)

@Serializable
data class FirestoreStringValue(
    val stringValue: String? = null
)