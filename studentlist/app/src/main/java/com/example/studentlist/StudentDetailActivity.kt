package com.example.studentlist

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class StudentDetailActivity : AppCompatActivity() {
    private lateinit var edtMSSV: EditText
    private lateinit var edtName: EditText
    private lateinit var edtPhone: EditText
    private lateinit var edtAddress: EditText
    private lateinit var btnUpdate: Button
    private lateinit var btnCancel: Button

    private var originalStudent: Student? = null
    private var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_detail)

        // Thiết lập action bar
        supportActionBar?.title = "Chi tiết sinh viên"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initializeViews()
        loadStudentData()
        setupButtons()
    }

    private fun initializeViews() {
        edtMSSV = findViewById(R.id.edtMSSV)
        edtName = findViewById(R.id.edtName)
        edtPhone = findViewById(R.id.edtPhone)
        edtAddress = findViewById(R.id.edtAddress)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnCancel = findViewById(R.id.btnCancel)
    }

    private fun loadStudentData() {
        originalStudent = intent.getParcelableExtra("STUDENT")
        position = intent.getIntExtra("POSITION", -1)

        originalStudent?.let { student ->
            edtMSSV.setText(student.id)
            edtName.setText(student.name)
            edtPhone.setText(student.phone)
            edtAddress.setText(student.address)

            // Không cho phép sửa MSSV
            edtMSSV.isEnabled = false
        }
    }

    private fun setupButtons() {
        btnUpdate.setOnClickListener {
            val name = edtName.text.toString().trim()
            val phone = edtPhone.text.toString().trim()
            val address = edtAddress.text.toString().trim()

            // Validate
            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Cập nhật sinh viên
            originalStudent?.let { student ->
                val updatedStudent = Student(student.id, name, phone, address)
                if (StudentManager.updateStudent(student.id, updatedStudent)) {
                    Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show()
                }
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