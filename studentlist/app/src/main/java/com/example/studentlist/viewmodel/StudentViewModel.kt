
package com.example.studentlist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.studentlist.data.StudentDatabaseHelper
import com.example.studentlist.model.Student

class StudentViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = StudentDatabaseHelper(application)

    private val _students = MutableLiveData<MutableList<Student>>()
    val students: LiveData<MutableList<Student>> get() = _students

    // Các biến Binding cho XML (Two-way binding)
    val mssv = MutableLiveData<String>("")
    val name = MutableLiveData<String>("")
    val sdt = MutableLiveData<String>("")
    val diachi = MutableLiveData<String>("")

    // Biến trạng thái
    private var isEditMode = false
    private var currentEditingMssv: String? = null // Lưu MSSV cũ để làm khóa cập nhật

    init {
        loadDataFromDb()
    }

    // Hàm load dữ liệu từ SQLite
    private fun loadDataFromDb() {
        _students.value = dbHelper.getAllStudents()
    }

    fun setModeAdd() {
        isEditMode = false
        currentEditingMssv = null
        resetForm()
    }

    fun setModeEdit(student: Student) {
        isEditMode = true
        currentEditingMssv = student.mssv // Quan trọng: lưu MSSV gốc

        mssv.value = student.mssv
        name.value = student.name
        sdt.value = student.sdt
        diachi.value = student.diachi
    }

    fun saveStudent(): Boolean {
        val curMssv = mssv.value?.trim() ?: ""
        val curName = name.value?.trim() ?: ""

        if (curMssv.isBlank() || curName.isBlank()) return false

        val studentInfo = Student(
            mssv = curMssv,
            name = curName,
            sdt = sdt.value?.trim() ?: "",
            diachi = diachi.value?.trim() ?: ""
        )

        if (isEditMode) {
            if (currentEditingMssv != null) {

                if (curMssv != currentEditingMssv && dbHelper.isStudentExists(curMssv)) {
                    return false
                }

                dbHelper.updateStudent(currentEditingMssv!!, studentInfo)
            }
        } else {
            if (dbHelper.isStudentExists(curMssv)) {
                return false
            }
            dbHelper.insertStudent(studentInfo)
        }

        loadDataFromDb()
        resetForm()
        return true
    }

    fun resetForm() {
        mssv.value = ""
        name.value = ""
        sdt.value = ""
        diachi.value = ""
    }
}