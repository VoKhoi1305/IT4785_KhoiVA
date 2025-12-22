package com.example.studentlist

object StudentRepository {
    val studentList = mutableListOf(
        Student("2021001", "Nguyễn Văn A", "090111222", "Hà Nội"),
        Student("2021002", "Trần Thị B", "090333444", "Đà Nẵng")
    )

    fun addStudent(student: Student) {
        studentList.add(student)
    }

    fun getStudent(mssv: String): Student? {
        return studentList.find { it.mssv == mssv }
    }
}