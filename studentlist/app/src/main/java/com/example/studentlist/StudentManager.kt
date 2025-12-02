package com.example.studentlist

object StudentManager {
    val students = mutableListOf<Student>()

    init {
        // Load dữ liệu mẫu
        students.addAll(listOf(
            Student("20220001", "Nguyễn Văn A", "0912345601", "Hà Nội"),
            Student("20220002", "Trần Thị B", "0912345602", "Hồ Chí Minh"),
            Student("20220003", "Lê Văn C", "0912345603", "Đà Nẵng"),
            Student("20220004", "Phạm Thị D", "0912345604", "Hải Phòng"),
            Student("20220005", "Hoàng Văn E", "0912345605", "Cần Thơ")
        ))
    }

    fun addStudent(student: Student): Boolean {
        if (students.any { it.id == student.id }) {
            return false
        }
        students.add(student)
        return true
    }

    fun updateStudent(oldId: String, updatedStudent: Student): Boolean {
        val index = students.indexOfFirst { it.id == oldId }
        if (index != -1) {
            students[index] = updatedStudent
            return true
        }
        return false
    }

    fun deleteStudent(id: String): Boolean {
        return students.removeIf { it.id == id }
    }

    fun getStudent(id: String): Student? {
        return students.find { it.id == id }
    }
}