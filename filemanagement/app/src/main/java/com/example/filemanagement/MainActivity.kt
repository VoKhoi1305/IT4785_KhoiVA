package com.example.filemanagement

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.filemanagement.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // SỬA: Dùng List thay vì Array để dễ xử lý sắp xếp
    private var currentFiles: List<File> = ArrayList()
    private var currentPath: File = Environment.getExternalStorageDirectory()
    private lateinit var adapter: ArrayAdapter<String>
    private var selectedFile: File? = null
    private var clipboardFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (currentPath.absolutePath != Environment.getExternalStorageDirectory().absolutePath && currentPath.parentFile != null) {
                    loadFiles(currentPath.parentFile!!)
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)
            }
        }

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ArrayList())
        binding.listFiles.adapter = adapter
        registerForContextMenu(binding.listFiles)

        binding.listFiles.setOnItemClickListener { _, _, position, _ ->
            val file = currentFiles.getOrNull(position) ?: return@setOnItemClickListener
            if (file.isDirectory) {
                loadFiles(file)
            } else {
                openFile(file)
            }
        }

        loadFiles(currentPath)
    }

    private fun loadFiles(directory: File) {
        currentPath = directory
        binding.textPath.text = directory.path

        val files = directory.listFiles() ?: emptyArray()

        currentFiles = files.sortedWith(compareBy({ !it.isDirectory }, { it.name.lowercase() }))

        val fileNames = ArrayList<String>()
        currentFiles.forEach {
            val prefix = if (it.isDirectory) "📁 " else "📄 "
            fileNames.add(prefix + it.name)
        }

        adapter.clear()
        adapter.addAll(fileNames)
        adapter.notifyDataSetChanged()
    }

    private fun openFile(file: File) {
        val name = file.name.lowercase()
        if (name.endsWith(".txt")) {
            try {
                val content = file.readText()
                showDialog(file.name, content)
            } catch (e: Exception) {
                Toast.makeText(this, "Lỗi đọc file: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else if (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".bmp")) {
            showImageDialog(file)
        } else {
            Toast.makeText(this, "Không hỗ trợ mở file này", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(0, 1, 0, "Tạo thư mục mới")
        menu?.add(0, 2, 0, "Tạo file văn bản mới")
        menu?.add(0, 3, 0, "Dán file (Paste)")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            1 -> showCreateDialog(true)
            2 -> showCreateDialog(false)
            3 -> pasteFile()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        selectedFile = currentFiles.getOrNull(info.position)

        if (selectedFile != null) {
            menu?.setHeaderTitle(selectedFile!!.name)
            menu?.add(0, 101, 0, "Đổi tên")
            menu?.add(0, 102, 0, "Xóa")
            if (selectedFile!!.isFile) {
                menu?.add(0, 103, 0, "Sao chép")
            }
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val file = selectedFile ?: return super.onContextItemSelected(item)
        when (item.itemId) {
            101 -> showRenameDialog(file)
            102 -> {
                try {
                    file.deleteRecursively()
                    loadFiles(currentPath)
                    Toast.makeText(this, "Đã xóa", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this, "Không thể xóa", Toast.LENGTH_SHORT).show()
                }
            }
            103 -> {
                clipboardFile = file
                Toast.makeText(this, "Đã sao chép: ${file.name}", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun pasteFile() {
        val source = clipboardFile
        if (source == null || !source.exists()) {
            Toast.makeText(this, "Chưa sao chép file nào!", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val dest = File(currentPath, source.name)
            if (dest.exists()) {
                Toast.makeText(this, "File đã tồn tại!", Toast.LENGTH_SHORT).show()
                return
            }
            source.copyTo(dest, overwrite = true)
            loadFiles(currentPath)
            Toast.makeText(this, "Dán thành công", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Lỗi dán file: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showCreateDialog(isFolder: Boolean) {
        val input = EditText(this)
        input.hint = if (isFolder) "Tên thư mục" else "Tên file (vd: data.txt)"

        AlertDialog.Builder(this)
            .setTitle(if (isFolder) "Tạo thư mục mới" else "Tạo file mới")
            .setView(input)
            .setPositiveButton("Tạo") { _, _ ->
                val name = input.text.toString().trim()
                if (name.isNotEmpty()) {
                    val f = File(currentPath, name)
                    // SỬA: Thêm try-catch để tránh crash nếu không tạo được
                    try {
                        val success = if (isFolder) f.mkdir() else f.createNewFile()
                        if (success) {
                            loadFiles(currentPath)
                            Toast.makeText(this, "Tạo thành công", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Không tạo được (Có thể đã tồn tại)", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun showRenameDialog(file: File) {
        val input = EditText(this)
        input.setText(file.name)
        AlertDialog.Builder(this)
            .setTitle("Đổi tên thành:")
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                val newName = input.text.toString().trim()
                if (newName.isNotEmpty()) {
                    val newFile = File(file.parent, newName)
                    if (file.renameTo(newFile)) {
                        loadFiles(currentPath)
                        Toast.makeText(this, "Đổi tên thành công", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Đổi tên thất bại", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun showDialog(title: String, content: String) {
        val scrollView = ScrollView(this)
        val textView = TextView(this)
        textView.text = content
        textView.textSize = 16f
        textView.setPadding(40, 40, 40, 40)
        scrollView.addView(textView)

        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(scrollView)
            .setPositiveButton("Đóng", null)
            .show()
    }

    private fun showImageDialog(file: File) {
        val imgView = ImageView(this)
        imgView.setImageBitmap(BitmapFactory.decodeFile(file.path))
        imgView.adjustViewBounds = true
        AlertDialog.Builder(this)
            .setTitle(file.name)
            .setView(imgView)
            .setPositiveButton("Đóng", null)
            .show()
    }
}