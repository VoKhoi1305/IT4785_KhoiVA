package com.example.httpstudentlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class StudentAdapter(
    private var originalList: List<Student>,
    private val onClick: (Student) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    private var filteredList: List<Student> = originalList

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgThumbnail: ImageView = itemView.findViewById(R.id.imgThumbnail)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvMssv: TextView = itemView.findViewById(R.id.tvMssv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = filteredList[position]
        holder.tvName.text = student.hoten
        holder.tvMssv.text = student.mssv

        val imageUrl = if (student.thumbnail.startsWith("http")) {
            student.thumbnail
        } else {
            "https://lebavui.io.vn" + student.thumbnail
        }

        Glide.with(holder.itemView.context)
            .load(imageUrl) // Truyền link đã xử lý vào đây
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.stat_notify_error)
            .into(holder.imgThumbnail)

        holder.itemView.setOnClickListener { onClick(student) }
    }

    override fun getItemCount(): Int = filteredList.size

    // Hàm lọc tìm kiếm
    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            originalList
        } else {
            originalList.filter {
                it.hoten.contains(query, ignoreCase = true) ||
                        it.mssv.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }

    fun updateData(newList: List<Student>) {
        originalList = newList
        filteredList = newList
        notifyDataSetChanged()
    }
}