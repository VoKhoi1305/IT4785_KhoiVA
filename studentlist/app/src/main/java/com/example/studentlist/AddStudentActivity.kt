package com.example.studentlist

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddStudentActivity : AppCompatActivity() {
    private lateinit var edtMSSV: EditText
    private lateinit var edtName: EditText
    private lateinit var edtPhone: EditText
    private lateinit var edtAddress: EditText
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        // Thiết lập action bar
        supportActionBar?.title = "Thêm sinh viên"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initializeViews()
        setupButtons()
    }

    private fun initializeViews() {
        edtMSSV = findViewById(R.id.edtMSSV)
        edtName = findViewById(R.id.edtName)
        edtPhone = findViewById(R.id.edtPhone)
        edtAddress = findViewById(R.id.edtAddress)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)
    }

    private fun setupButtons() {
        btnSave.setOnClickListener {
            val mssv = edtMSSV.text.toString().trim()
            val name = edtName.text.toString().trim()
            val phone = edtPhone.text.toString().trim()
            val address = edtAddress.text.toString().trim()

            // Validate
            if (mssv.isEmpty() || name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Thêm sinh viên
            val newStudent = Student(mssv, name, phone, address)
            if (StudentManager.addStudent(newStudent)) {
                Toast.makeText(this, "Thêm sinh viên thành công", Toast.LENGTH_SHORT).show()
                finish() // Quay lại MainActivity
            } else {
                Toast.makeText(this, "Mã số sinh viên đã tồn tại", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}