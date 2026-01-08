package org.example.project.presentation


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.domain.model.Attendance
import org.example.project.domain.model.AttendanceStatus
import org.example.project.domain.model.ClassRoom
import org.example.project.domain.model.Student
import org.example.project.domain.repository.AttendanceRepository

class AttendanceViewModel( private val repo: AttendanceRepository): CommonViewModel() {

    private val _classes = MutableStateFlow<List<ClassRoom>>(emptyList())
    val classes = _classes.asStateFlow()

    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students = _students.asStateFlow()

    private val _attendance = MutableStateFlow<List<Attendance>>(emptyList())
    val attendance = _attendance.asStateFlow()

    fun loadClasses() {
        viewModelScope.launch {
            _classes.value = repo.getClasses()
        }
    }

    fun addClass(name: String) {
        viewModelScope.launch {
            repo.addClass(ClassRoom(name = name))
            loadClasses()
        }
    }

    fun loadStudents(classId: String) {
        viewModelScope.launch {
            _students.value = repo.getStudents(classId)
        }
    }

    fun addStudent(classId: String, name: String) {
        viewModelScope.launch {
            repo.addStudent(classId, Student(name = name))
            loadStudents(classId)
        }
    }

    fun loadAttendance(classId: String, date: String) {
        viewModelScope.launch {
            _attendance.value = repo.getAttendance(classId, date)
        }
    }

    fun toggleAttendance(classId: String, date: String, studentId: String) {
        viewModelScope.launch {
            val list = _attendance.value.toMutableList()
            val index = list.indexOfFirst { it.studentId == studentId }

            if (index >= 0) {
                val old = list[index]
                val updated = old.copy(
                    status = if (old.status == AttendanceStatus.PRESENT)
                        AttendanceStatus.ABSENT else AttendanceStatus.PRESENT
                )
                repo.updateAttendance(classId, date = date, record = updated)
                list[index] = updated
            }

            _attendance.value = list
        }
    }
}




open class CommonViewModel {
    protected val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    open fun onCleared() {
        viewModelScope.coroutineContext.cancel()
    }
}
