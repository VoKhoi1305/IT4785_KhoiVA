package com.example.number // Gói của bạn

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import kotlin.math.sqrt
import com.example.number.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var radioButtons: List<RadioButton>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        radioButtons = listOf(
            binding.rbOdd, binding.rbPrime, binding.rbPerfect,
            binding.rbEven, binding.rbPerfectSquare, binding.rbFibonacci
        )

        binding.etInputNumber.addTextChangedListener {
            updateNumberList()
        }

        val clickListener = View.OnClickListener { clickedView ->
            // Bỏ chọn tất cả các nút
            for (rb in radioButtons) {
                if (rb.id != clickedView.id) {
                    rb.isChecked = false
                }
            }
            (clickedView as RadioButton).isChecked = true

            updateNumberList()
        }

        radioButtons.forEach { rb ->
            rb.setOnClickListener(clickListener)
        }

        updateNumberList()
    }

    private fun updateNumberList() {
        val limitText = binding.etInputNumber.text.toString()
        val limit = limitText.toIntOrNull() ?: 0


        val selectedId = when {
            binding.rbOdd.isChecked -> R.id.rbOdd
            binding.rbEven.isChecked -> R.id.rbEven
            binding.rbPrime.isChecked -> R.id.rbPrime
            binding.rbPerfectSquare.isChecked -> R.id.rbPerfectSquare
            binding.rbPerfect.isChecked -> R.id.rbPerfect
            binding.rbFibonacci.isChecked -> R.id.rbFibonacci
            else -> -1
        }


        val generatedNumbers = when (selectedId) {
            R.id.rbOdd -> generateOddNumbers(limit)
            R.id.rbEven -> generateEvenNumbers(limit)
            R.id.rbPrime -> generatePrimeNumbers(limit)
            R.id.rbPerfectSquare -> generatePerfectSquares(limit)
            R.id.rbPerfect -> generatePerfectNumbers(limit)
            R.id.rbFibonacci -> generateFibonacciNumbers(limit)
            else -> emptyList()
        }


        binding.llNumberContainer.removeAllViews()


        if (generatedNumbers.isEmpty()) {
            binding.scrollView.visibility = View.GONE
            binding.tvNoResults.visibility = View.VISIBLE
        } else {
            binding.scrollView.visibility = View.VISIBLE
            binding.tvNoResults.visibility = View.GONE


            for (number in generatedNumbers) {

                val textView = TextView(this).apply {
                    text = number.toString()
                    textSize = 18f
                    // Hàm dpToPx() là hàm tự định nghĩa bên dưới
                    setPadding(16.dpToPx(), 12.dpToPx(), 16.dpToPx(), 12.dpToPx())
                }
                binding.llNumberContainer.addView(textView)


                val divider = View(this).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, // Chiều rộng
                        1.dpToPx() // Chiều cao 1dp
                    )
                    setBackgroundColor(Color.parseColor("#E0E0E0")) // Màu xám nhạt
                }
                binding.llNumberContainer.addView(divider)
            }
        }
    }


    private fun generateOddNumbers(limit: Int): List<Int> {
        return (1 until limit step 2).toList()
    }

    private fun generateEvenNumbers(limit: Int): List<Int> {
        return (2 until limit step 2).toList()
    }

    private fun generatePerfectSquares(limit: Int): List<Int> {
        val list = mutableListOf<Int>()
        var i = 1
        while (i * i < limit) {
            list.add(i * i)
            i++
        }
        return list
    }

    private fun generateFibonacciNumbers(limit: Int): List<Int> {
        val list = mutableListOf<Int>()
        var a = 0
        var b = 1
        while (b < limit) {
            list.add(b)
            val next = a + b
            a = b
            b = next
        }
        return list.distinct()
    }

    private fun generatePrimeNumbers(limit: Int): List<Int> {
        val list = mutableListOf<Int>()
        for (i in 2 until limit) {
            if (isPrime(i)) {
                list.add(i)
            }
        }
        return list
    }

    private fun isPrime(n: Int): Boolean {
        if (n < 2) return false
        for (i in 2..sqrt(n.toDouble()).toInt()) {
            if (n % i == 0) return false
        }
        return true
    }

    private fun generatePerfectNumbers(limit: Int): List<Int> {
        val list = mutableListOf<Int>()
        val perfects = listOf(6, 28, 496, 8128)
        for (p in perfects) {
            if (p < limit) {
                list.add(p)
            } else {
                break
            }
        }
        return list
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }
}