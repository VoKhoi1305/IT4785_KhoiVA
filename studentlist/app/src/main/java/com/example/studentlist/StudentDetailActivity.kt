package com.example.studentlist

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class StudentDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_detail)

        val toolbar = findViewById<Toolbar>(R.id.toolbarDetail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Chi Tiết Sinh Viên"

        toolbar.setNavigationOnClickListener {
            finish()
        }

        // 2. XỬ LÝ LOGIC CẬP NHẬT
        val etMSSV = findViewById<EditText>(R.id.etDetailMSSV)
        val etHoten = findViewById<EditText>(R.id.etDetailHoten)
        val etSDT = findViewById<EditText>(R.id.etDetailSDT)
        val etDiaChi = findViewById<EditText>(R.id.etDetailDiaChi)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)

        val mssv = intent.getStringExtra("MSSV")
        val student = StudentRepository.getStudent(mssv ?: "")

        if (student != null) {
            etMSSV.setText(student.mssv)
            etHoten.setText(student.hoTen)
            etSDT.setText(student.soDienThoai)
            etDiaChi.setText(student.diaChi)
        }

        btnUpdate.setOnClickListener {
            if (student != null) {
                student.hoTen = etHoten.text.toString()
                student.soDienThoai = etSDT.text.toString()
                student.diaChi = etDiaChi.text.toString()

                Toast.makeText(this, "Đã cập nhật thông tin!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Lỗi: Không tìm thấy sinh viên", Toast.LENGTH_SHORT).show()
            }
        }
    }
}