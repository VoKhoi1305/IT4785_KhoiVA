package com.example.httpstudentlist

import android.content.Intent
import android.os.Bundle
// 1. CHÚ Ý IMPORT CÁC DÒNG NÀY
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    // 2. KHAI BÁO BIẾN Ở ĐÂY (Cấp độ Class) để dùng được mọi nơi
    private lateinit var adapter: StudentAdapter
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView // <--- Sửa lỗi Unresolved reference recyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 3. ÁNH XẠ VIEW (Gán giá trị cho biến đã khai báo ở trên)
        // Lưu ý: Không dùng từ khóa 'val' hay 'var' ở đầu dòng nữa
        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)

        // Cấu hình RecyclerView
        adapter = StudentAdapter(listOf()) { student ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("STUDENT_DATA", student)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Cấu hình Search
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText ?: "")
                return true
            }
        })

        fetchData()
    }

    private fun fetchData() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://lebavui.io.vn/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(StudentApi::class.java)

        api.getStudents().enqueue(object : Callback<List<Student>> {
            override fun onResponse(call: Call<List<Student>>, response: Response<List<Student>>) {
                if (response.isSuccessful) {
                    val students = response.body() ?: emptyList()
                    adapter.updateData(students)
                } else {
                    Toast.makeText(this@MainActivity, "Lỗi server: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Student>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Lỗi mạng: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}