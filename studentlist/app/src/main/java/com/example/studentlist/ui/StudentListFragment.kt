package com.example.studentlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentlist.R
import com.example.studentlist.databinding.FragmentStudentListBinding
import com.example.studentlist.viewmodel.StudentViewModel

class StudentListFragment : Fragment() {

    private val viewModel: StudentViewModel by activityViewModels()
    private lateinit var binding: FragmentStudentListBinding
    private lateinit var adapter: StudentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentListBinding.inflate(inflater, container, false)

        adapter = StudentAdapter(emptyList()) { selectedStudent ->
            viewModel.setModeEdit(selectedStudent)


            findNavController().navigate(R.id.addStudentFragment)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        viewModel.students.observe(viewLifecycleOwner) { list ->
            adapter.updateData(list)
        }

        binding.fabAdd.setOnClickListener {
            viewModel.setModeAdd()

            findNavController().navigate(R.id.addStudentFragment)
        }

        return binding.root
    }
}