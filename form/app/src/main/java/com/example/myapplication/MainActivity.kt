package com.example.myapplication

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var defaultEditTextBg: Drawable
    private lateinit var errorEditTextBg: Drawable
    private lateinit var defaultRadioGroupBg: Drawable
    private lateinit var errorRadioGroupBg: Drawable
    private var defaultCheckBoxColor: Int = 0

    private val handler = Handler(Looper.getMainLooper())
    private var resetRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Lưu background/màu gốc và tạo background lỗi
        defaultEditTextBg = ColorDrawable(Color.parseColor("#F0F0F0"))
        errorEditTextBg = ColorDrawable(Color.RED)
        defaultRadioGroupBg = binding.rgGender.background ?: ColorDrawable(Color.TRANSPARENT)
        errorRadioGroupBg = ColorDrawable(Color.RED)
        defaultCheckBoxColor = binding.cbTerms.currentTextColor

        setupListeners()
        setupTextWatchers()
    }

    private fun setupListeners() {
        // Sự kiện nút Select - Toggle CalendarContainer
        binding.btnSelectDate.setOnClickListener {
            toggleCalendarView()
        }

        // Sự kiện chọn ngày trên CalendarView
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            binding.etBirthday.setText(selectedDate)

            // Reset màu đỏ của Birthday khi chọn ngày
            binding.etBirthday.background = defaultEditTextBg

            // Ẩn CalendarContainer sau khi chọn xong
            binding.calendarContainer.visibility = View.GONE
        }

        // Sự kiện nút Register
        binding.btnRegister.setOnClickListener {
            if (validateInputs()) {
                Toast.makeText(this, "Registration Successful!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Please fill all required fields.", Toast.LENGTH_SHORT).show()

                // Tự động xóa màu đỏ sau 5 giây
                scheduleResetErrors()
            }
        }

        // Lắng nghe thay đổi trên RadioGroup Gender
        binding.rgGender.setOnCheckedChangeListener { _, _ ->
            // Khi chọn gender, reset màu đỏ
            binding.rgGender.background = defaultRadioGroupBg
        }

        // Lắng nghe thay đổi trên CheckBox Terms
        binding.cbTerms.setOnCheckedChangeListener { _, _ ->
            // Khi click checkbox, reset màu đỏ
            binding.cbTerms.setTextColor(defaultCheckBoxColor)
        }
    }

    /**
     * Thiết lập TextWatcher cho các EditText để tự động xóa màu đỏ khi nhập
     */
    private fun setupTextWatchers() {
        // TextWatcher cho First Name
        binding.etFirstName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Xóa màu đỏ ngay khi bắt đầu nhập
                if (!s.isNullOrEmpty()) {
                    binding.etFirstName.background = defaultEditTextBg
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // TextWatcher cho Last Name
        binding.etLastName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    binding.etLastName.background = defaultEditTextBg
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // TextWatcher cho Birthday
        binding.etBirthday.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    binding.etBirthday.background = defaultEditTextBg
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // TextWatcher cho Address
        binding.etAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    binding.etAddress.background = defaultEditTextBg
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // TextWatcher cho Email
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    binding.etEmail.background = defaultEditTextBg
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    /**
     * Toggle hiển thị CalendarContainer
     */
    private fun toggleCalendarView() {
        if (binding.calendarContainer.visibility == View.VISIBLE) {
            binding.calendarContainer.visibility = View.GONE
        } else {
            binding.calendarContainer.visibility = View.VISIBLE
        }
    }

    /**
     * Validate tất cả input fields
     */
    private fun validateInputs(): Boolean {
        var isValid = true

        // Kiểm tra First Name
        if (binding.etFirstName.text.isNullOrEmpty()) {
            binding.etFirstName.background = errorEditTextBg
            isValid = false
        }

        // Kiểm tra Last Name
        if (binding.etLastName.text.isNullOrEmpty()) {
            binding.etLastName.background = errorEditTextBg
            isValid = false
        }

        // Kiểm tra Gender
        if (binding.rgGender.checkedRadioButtonId == -1) {
            binding.rgGender.background = errorRadioGroupBg
            isValid = false
        }

        // Kiểm tra Birthday
        if (binding.etBirthday.text.isNullOrEmpty()) {
            binding.etBirthday.background = errorEditTextBg
            isValid = false
        }

        // Kiểm tra Address
        if (binding.etAddress.text.isNullOrEmpty()) {
            binding.etAddress.background = errorEditTextBg
            isValid = false
        }

        // Kiểm tra Email
        if (binding.etEmail.text.isNullOrEmpty()) {
            binding.etEmail.background = errorEditTextBg
            isValid = false
        }

        // Kiểm tra Terms checkbox
        if (!binding.cbTerms.isChecked) {
            binding.cbTerms.setTextColor(Color.RED)
            isValid = false
        }

        return isValid
    }

    /**
     * Lên lịch để tự động reset màu đỏ sau 5 giây
     */
    private fun scheduleResetErrors() {
        // Hủy runnable cũ nếu có
        resetRunnable?.let { handler.removeCallbacks(it) }

        // Tạo runnable mới
        resetRunnable = Runnable {
            resetErrorBackgrounds()
        }

        // Delay 5000ms = 5 giây
        handler.postDelayed(resetRunnable!!, 5000)
    }

    /**
     * Reset tất cả các error backgrounds về trạng thái ban đầu
     */
    private fun resetErrorBackgrounds() {
        binding.etFirstName.background = defaultEditTextBg
        binding.etLastName.background = defaultEditTextBg
        binding.etBirthday.background = defaultEditTextBg
        binding.etAddress.background = defaultEditTextBg
        binding.etEmail.background = defaultEditTextBg

        binding.rgGender.background = defaultRadioGroupBg
        binding.cbTerms.setTextColor(defaultCheckBoxColor)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Dọn dẹp handler khi Activity bị destroy
        resetRunnable?.let { handler.removeCallbacks(it) }
    }
}