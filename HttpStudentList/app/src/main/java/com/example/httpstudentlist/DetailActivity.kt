package com.example.httpstudentlist

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val student = intent.getSerializableExtra("STUDENT_DATA") as? Student

        if (student != null) {
            findViewById<TextView>(R.id.tvDetailName).text = student.hoten
            findViewById<TextView>(R.id.tvDetailMssv).text = "MSSV: ${student.mssv}"
            findViewById<TextView>(R.id.tvDetailDob).text = "Ngày sinh: ${student.ngaysinh}"
            findViewById<TextView>(R.id.tvDetailEmail).text = "Email: ${student.email}"

            val img = findViewById<ImageView>(R.id.imgDetailAvatar)

            val imageUrl = if (student.thumbnail.startsWith("http")) {
                student.thumbnail
            } else {
                "https://lebavui.io.vn" + student.thumbnail
            }

            Glide.with(this)
                .load(imageUrl)
                .into(img)
        }
    }
}