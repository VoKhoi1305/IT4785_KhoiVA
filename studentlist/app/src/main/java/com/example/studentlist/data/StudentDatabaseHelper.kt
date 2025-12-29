package com.example.studentlist.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.studentlist.model.Student

class StudentDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "StudentManager.db"
        private const val DATABASE_VERSION = 1

        // Tên bảng và các cột
        const val TABLE_STUDENTS = "students"
        const val COLUMN_MSSV = "mssv"
        const val COLUMN_NAME = "name"
        const val COLUMN_SDT = "sdt"
        const val COLUMN_DIACHI = "diachi"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE $TABLE_STUDENTS ("
                + "$COLUMN_MSSV TEXT PRIMARY KEY,"
                + "$COLUMN_NAME TEXT,"
                + "$COLUMN_SDT TEXT,"
                + "$COLUMN_DIACHI TEXT)")
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Xóa bảng cũ nếu tồn tại và tạo lại (đơn giản hóa cho bài tập)
        db.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENTS")
        onCreate(db)
    }

    // --- CÁC HÀM CRUD ---

    // 1. Thêm sinh viên
    fun insertStudent(student: Student): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_MSSV, student.mssv)
            put(COLUMN_NAME, student.name)
            put(COLUMN_SDT, student.sdt)
            put(COLUMN_DIACHI, student.diachi)
        }
        val result = db.insert(TABLE_STUDENTS, null, values)
        db.close()
        return result
    }

    // 2. Lấy danh sách tất cả sinh viên
    fun getAllStudents(): MutableList<Student> {
        val list = mutableListOf<Student>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_STUDENTS", null)

        if (cursor.moveToFirst()) {
            do {
                val mssv = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MSSV))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val sdt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SDT))
                val diachi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIACHI))
                list.add(Student(mssv, name, sdt, diachi))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    // 3. Cập nhật sinh viên (Dựa vào MSSV gốc trước khi sửa)
    fun updateStudent(originalMssv: String, newInfo: Student): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_MSSV, newInfo.mssv) // Có thể sửa cả MSSV
            put(COLUMN_NAME, newInfo.name)
            put(COLUMN_SDT, newInfo.sdt)
            put(COLUMN_DIACHI, newInfo.diachi)
        }
        // Cập nhật dòng có mssv cũ
        val result = db.update(TABLE_STUDENTS, values, "$COLUMN_MSSV = ?", arrayOf(originalMssv))
        db.close()
        return result
    }

    // 4. Kiểm tra sinh viên tồn tại
    fun isStudentExists(mssv: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT 1 FROM $TABLE_STUDENTS WHERE $COLUMN_MSSV = ?", arrayOf(mssv))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }
}