package com.example.studentlist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studentlist.model.Student

class StudentViewModel : ViewModel() {
    private val _students = MutableLiveData<MutableList<Student>>()
    val students: LiveData<MutableList<Student>> get() = _students

    // Các biến Binding
    val mssv = MutableLiveData<String>("")
    val name = MutableLiveData<String>("")
    val sdt = MutableLiveData<String>("")
    val diachi = MutableLiveData<String>("")

    // Biến để xác định đang ở chế độ Sửa hay Thêm
    private var isEditMode = false
    private var currentEditingMssv: String? = null

    init {
        // Data mẫu
        _students.value = mutableListOf(
            Student("2021001", "Nguyễn Văn A", "090111222", "Hà Nội"),
            Student("2021002", "Trần Thị B", "090333444", "Đà Nẵng")
        )
    }

    // Hàm 1: Gọi khi nhấn vào nút "Thêm sinh viên"
    fun setModeAdd() {
        isEditMode = false
        currentEditingMssv = null
        resetForm()
    }

    // Hàm 2: Gọi khi nhấn vào một sinh viên trong danh sách (để sửa)
    fun setModeEdit(student: Student) {
        isEditMode = true
        currentEditingMssv = student.mssv

        // Đổ dữ liệu cũ lên form
        mssv.value = student.mssv
        name.value = student.name
        sdt.value = student.sdt
        diachi.value = student.diachi
    }

    // Hàm 3: Lưu (Xử lý cả Thêm và Sửa)
    fun saveStudent(): Boolean {
        val curMssv = mssv.value ?: ""
        val curName = name.value ?: ""

        if (curMssv.isBlank() || curName.isBlank()) return false

        val newInfo = Student(
            mssv = curMssv,
            name = curName,
            sdt = sdt.value ?: "",
            diachi = diachi.value ?: ""
        )

        val currentList = _students.value ?: mutableListOf()

        if (isEditMode) {
            // Logic CẬP NHẬT: Tìm sinh viên có MSSV cũ và thay thế
            val index = currentList.indexOfFirst { it.mssv == currentEditingMssv }
            if (index != -1) {
                currentList[index] = newInfo
            }
        } else {
            // Logic THÊM MỚI: Kiểm tra trùng MSSV
            if (currentList.any { it.mssv == curMssv }) return false // Đã tồn tại
            currentList.add(newInfo)
        }

        _students.value = currentList
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