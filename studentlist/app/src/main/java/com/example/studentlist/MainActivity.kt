package com.example.studentlist

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)
        setupListView()
    }

    override fun onResume() {
        super.onResume()
        // Refresh danh sách mỗi khi quay lại activity này
        adapter.notifyDataSetChanged()
    }

    private fun setupListView() {
        adapter = StudentAdapter()
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val student = StudentManager.students[position]
            val intent = Intent(this, StudentDetailActivity::class.java)
            intent.putExtra("STUDENT", student)
            intent.putExtra("POSITION", position)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add_student -> {
                val intent = Intent(this, AddStudentActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    inner class StudentAdapter : BaseAdapter() {
        override fun getCount(): Int = StudentManager.students.size

        override fun getItem(position: Int): Any = StudentManager.students[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view: View
            val tvName: TextView
            val tvId: TextView
            val btnDelete: ImageButton

            if (convertView == null) {
                view = LayoutInflater.from(this@MainActivity)
                    .inflate(R.layout.item_student, parent, false)
                tvName = view.findViewById(R.id.tvName)
                tvId = view.findViewById(R.id.tvId)
                btnDelete = view.findViewById(R.id.btnDelete)

                view.tag = ViewHolder(tvName, tvId, btnDelete)
            } else {
                view = convertView
                val holder = view.tag as ViewHolder
                tvName = holder.tvName
                tvId = holder.tvId
                btnDelete = holder.btnDelete
            }

            val student = StudentManager.students[position]
            tvName.text = student.name
            tvId.text = student.id

            btnDelete.setOnClickListener {
                val deletedStudent = StudentManager.students[position]
                StudentManager.deleteStudent(deletedStudent.id)
                notifyDataSetChanged()
                Toast.makeText(
                    this@MainActivity,
                    "Đã xóa: ${deletedStudent.name}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            return view
        }
    }

    class ViewHolder(
        val tvName: TextView,
        val tvId: TextView,
        val btnDelete: ImageButton
    )
}