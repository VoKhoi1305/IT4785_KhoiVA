package com.example.calculator

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var textDisplay: TextView

    private var currentNumber = ""
    private var previousNumber = ""
    private var operation = ""
    private var isNewOperation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textDisplay = binding.textDisplay

        val numberButtons = listOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3, binding.btn4,
            binding.btn5, binding.btn6, binding.btn7, binding.btn8, binding.btn9
        )
        numberButtons.forEach { button ->
            button.setOnClickListener {
                onNumberClick(button.text.toString())
            }
        }

        binding.btnAdd.setOnClickListener { onOperationClick("+") }
        binding.btnSubtract.setOnClickListener { onOperationClick("-") }
        binding.btnMultiply.setOnClickListener { onOperationClick("×") }
        binding.btnDivide.setOnClickListener { onOperationClick("÷") }
        binding.btnEquals.setOnClickListener { onEqualsClick() }
        binding.btnCE.setOnClickListener { onCEClick() }
        binding.btnC.setOnClickListener { onCClick() }
        binding.btnBS.setOnClickListener { onBSClick() }
        binding.btnDecimal.setOnClickListener { onDecimalClick() }
        binding.btnPlusMinus.setOnClickListener { onPlusMinusClick() }
    }

    private fun onNumberClick(number: String) {
        if (isNewOperation) {
            currentNumber = number
            isNewOperation = false
        } else {
            currentNumber += number
        }
        updateDisplay(currentNumber)
    }

    // Xử lý khi nhấn nút phép toán
    private fun onOperationClick(op: String) {
        if (currentNumber.isNotEmpty()) {
            if (previousNumber.isNotEmpty() && !isNewOperation) {
                calculate()
            }
            previousNumber = currentNumber
            operation = op
            isNewOperation = true
        } else if (previousNumber.isNotEmpty()) {
            // Cho phép thay đổi phép toán
            operation = op
        }
    }

    // Xử lý khi nhấn nút bằng
    private fun onEqualsClick() {
        if (previousNumber.isNotEmpty() && currentNumber.isNotEmpty() && operation.isNotEmpty()) {
            calculate()
            operation = ""
            previousNumber = ""
            isNewOperation = true
        }
    }

    // Thực hiện phép tính
    private fun calculate() {
        try {
            val num1 = previousNumber.toDouble()
            val num2 = currentNumber.toDouble()

            val result = when (operation) {
                "+" -> num1 + num2
                "-" -> num1 - num2
                "×" -> num1 * num2
                "÷" -> {
                    if (num2 != 0.0) num1 / num2
                    else {
                        updateDisplay("Error")
                        resetCalculator()
                        return
                    }
                }
                else -> return
            }

            // Chuyển kết quả thành số nguyên nếu là số nguyên
            currentNumber = if (result % 1.0 == 0.0) {
                result.toInt().toString()
            } else {
                result.toString()
            }

            updateDisplay(currentNumber)
        } catch (e: Exception) {
            updateDisplay("Error")
            resetCalculator()
        }
    }

    // Nút CE: Xóa giá trị toán hạng hiện tại về 0
    private fun onCEClick() {
        currentNumber = "0"
        updateDisplay(currentNumber)
        isNewOperation = true
    }

    // Nút C: Xóa phép toán, nhập lại từ đầu
    private fun onCClick() {
        resetCalculator()
        updateDisplay("0")
    }

    // Nút BS: Xóa chữ số hàng đơn vị
    private fun onBSClick() {
        if (currentNumber.isNotEmpty() && currentNumber != "0") {
            currentNumber = if (currentNumber.length == 1) {
                "0"
            } else {
                currentNumber.dropLast(1)
            }
            updateDisplay(currentNumber)
        }
    }

    // Xử lý nút dấu thập phân
    private fun onDecimalClick() {
        if (isNewOperation) {
            currentNumber = "0."
            isNewOperation = false
        } else if (!currentNumber.contains(".")) {
            currentNumber += "."
        }
        updateDisplay(currentNumber)
    }

    // Xử lý nút +/-
    private fun onPlusMinusClick() {
        if (currentNumber.isNotEmpty() && currentNumber != "0") {
            currentNumber = if (currentNumber.startsWith("-")) {
                currentNumber.substring(1)
            } else {
                "-$currentNumber"
            }
            updateDisplay(currentNumber)
        }
    }

    // Cập nhật hiển thị
    private fun updateDisplay(value: String) {
        textDisplay.text = value
    }

    // Reset calculator
    private fun resetCalculator() {
        currentNumber = ""
        previousNumber = ""
        operation = ""
        isNewOperation = true
    }
}