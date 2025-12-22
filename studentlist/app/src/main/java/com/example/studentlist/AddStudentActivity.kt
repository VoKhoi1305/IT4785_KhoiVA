package com.example.studentlist

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class AddStudentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        val toolbar = findViewById<Toolbar>(R.id.toolbarAdd)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Hiện nút mũi tên
        supportActionBar?.title = "Thêm Sinh Viên Mới"

        toolbar.setNavigationOnClickListener {
            finish()
        }

        val etMSSV = findViewById<EditText>(R.id.etMSSV)
        val etHoten = findViewById<EditText>(R.id.etHoten)
        val etSDT = findViewById<EditText>(R.id.etSDT)
        val etDiaChi = findViewById<EditText>(R.id.etDiaChi)
        val btnAdd = findViewById<Button>(R.id.btnAdd)

        btnAdd.setOnClickListener {
            val mssv = etMSSV.text.toString().trim()
            val hoten = etHoten.text.toString().trim()
            val sdt = etSDT.text.toString().trim()
            val diachi = etDiaChi.text.toString().trim()

            if (mssv.isEmpty() || hoten.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập MSSV và Họ tên", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (StudentRepository.getStudent(mssv) != null) {
                Toast.makeText(this, "MSSV đã tồn tại!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newStudent = Student(mssv, hoten, sdt, diachi)
            StudentRepository.addStudent(newStudent)

            Toast.makeText(this, "Đã thêm thành công", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}