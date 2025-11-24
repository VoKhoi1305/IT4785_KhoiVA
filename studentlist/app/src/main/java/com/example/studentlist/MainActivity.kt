package com.example.studentlist


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

data class Student(
    val id: String,
    var name: String
)

class MainActivity : AppCompatActivity() {
    private lateinit var edtMSSV: EditText
    private lateinit var edtName: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnUpdate: Button
    private lateinit var listView: ListView

    private val studentList = mutableListOf<Student>()
    private lateinit var adapter: StudentAdapter
    private var selectedPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        loadSampleData()
        setupListView()
        setupButtons()
    }

    private fun initializeViews() {
        edtMSSV = findViewById(R.id.edtMSSV)
        edtName = findViewById(R.id.edtName)
        btnAdd = findViewById(R.id.btnAdd)
        btnUpdate = findViewById(R.id.btnUpdate)
        listView = findViewById(R.id.listView)
    }

    private fun loadSampleData() {
        studentList.addAll(listOf(
            Student("20200001", "Nguyễn Văn A"),
            Student("20200002", "Trần Thị B"),
            Student("20200003", "Lê Văn C"),
            Student("20200004", "Phạm Thị D"),
            Student("20200005", "Hoàng Văn E"),
            Student("20200006", "Vũ Thị F"),
            Student("20200007", "Đặng Văn G"),
            Student("20200008", "Bùi Thị H"),
            Student("20200009", "Hồ Văn I")
        ))
    }

    private fun setupListView() {
        adapter = StudentAdapter()
        listView.adapter = adapter

        listView.setOnItemClickListener { _, view, position, _ ->
            selectedPosition = position
            val student = studentList[position]
            edtMSSV.setText(student.id)
            edtName.setText(student.name)
            edtMSSV.isEnabled = false
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "Đã chọn: ${student.name}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupButtons() {
        btnAdd.setOnClickListener {
            val mssv = edtMSSV.text.toString().trim()
            val name = edtName.text.toString().trim()

            if (mssv.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (studentList.any { it.id == mssv }) {
                Toast.makeText(this, "Mã số sinh viên đã tồn tại", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            studentList.add(Student(mssv, name))
            adapter.notifyDataSetChanged()
            clearInputs()
            Toast.makeText(this, "Thêm sinh viên thành công", Toast.LENGTH_SHORT).show()
        }

        btnUpdate.setOnClickListener {
            if (selectedPosition == -1) {
                Toast.makeText(this, "Vui lòng chọn sinh viên cần cập nhật", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val name = edtName.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập họ tên", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            studentList[selectedPosition].name = name
            adapter.notifyDataSetChanged()
            clearInputs()
            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearInputs() {
        edtMSSV.text.clear()
        edtName.text.clear()
        edtMSSV.isEnabled = true
        selectedPosition = -1
        adapter.notifyDataSetChanged()
    }

    inner class StudentAdapter : BaseAdapter() {
        override fun getCount(): Int = studentList.size

        override fun getItem(position: Int): Any = studentList[position]

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

            val student = studentList[position]
            tvName.text = student.name
            tvId.text = student.id

            // Highlight item được chọn
            if (position == selectedPosition) {
                view.setBackgroundColor(0xFFE3F2FD.toInt())
            } else {
                view.setBackgroundColor(0xFFFFFFFF.toInt())
            }

            // Xử lý click vào nút Delete
            btnDelete.setOnClickListener {
                val deletedStudent = studentList[position]
                studentList.removeAt(position)

                if (selectedPosition == position) {
                    selectedPosition = -1
                    clearInputs()
                } else if (selectedPosition > position) {
                    selectedPosition--
                }

                notifyDataSetChanged()
                Toast.makeText(this@MainActivity, "Đã xóa: ${deletedStudent.name}", Toast.LENGTH_SHORT).show()
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