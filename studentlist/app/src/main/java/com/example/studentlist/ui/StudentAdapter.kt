package com.example.studentlist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studentlist.databinding.ItemStudentBinding
import com.example.studentlist.model.Student

class StudentAdapter(
    private var studentList: List<Student>,
    // Thêm callback khi click vào item để sửa
    private val onStudentClick: (Student) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    inner class StudentViewHolder(val binding: ItemStudentBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                // Gọi callback khi click vào dòng
                onStudentClick(studentList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.binding.tvName.text = student.name
        holder.binding.tvMssv.text = "MSSV: ${student.mssv}"
        holder.binding.tvPhone.text = "SĐT: ${student.sdt}"
        holder.binding.tvAddress.text = "ĐC: ${student.diachi}"
    }

    override fun getItemCount(): Int = studentList.size

    fun updateData(newStudentList: List<Student>) {
        this.studentList = newStudentList
        notifyDataSetChanged()
    }
}