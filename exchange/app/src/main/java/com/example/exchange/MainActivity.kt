package com.example.exchange

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.exchange.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // ViewBinding
    private lateinit var binding: ActivityMainBinding

    // Danh sách tiền tệ (10 loại)
    private val currencies = arrayOf(
        "VND - Việt Nam Đồng",
        "USD - Đô la Mỹ",
        "EUR - Euro",
        "GBP - Bảng Anh",
        "JPY - Yên Nhật",
        "CNY - Nhân dân tệ",
        "KRW - Won Hàn Quốc",
        "THB - Baht Thái Lan",
        "SGD - Đô la Singapore",
        "AUD - Đô la Úc"
    )


    private val currencyCodes = arrayOf(
        "VND", "USD", "EUR", "GBP", "JPY", "CNY", "KRW", "THB", "SGD", "AUD"
    )


    private val exchangeRates = mapOf(
        "VND" to 24000.0,
        "USD" to 1.0,
        "EUR" to 0.92,
        "GBP" to 0.79,
        "JPY" to 149.5,
        "CNY" to 7.24,
        "KRW" to 1320.0,
        "THB" to 35.5,
        "SGD" to 1.34,
        "AUD" to 1.53
    )

    private var isUpdatingFromAmount = false
    private var isUpdatingToAmount = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupSpinners()


        setupListeners()
    }

    private fun setupSpinners() {

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        binding.spinnerFromCurrency.adapter = adapter
        binding.spinnerToCurrency.adapter = adapter

        // Thiết lập giá trị mặc định: USD -> VND
        binding.spinnerFromCurrency.setSelection(1) // USD
        binding.spinnerToCurrency.setSelection(0) // VND
    }

    private fun setupListeners() {
        // Listener cho EditText "Từ"
        binding.editTextFromAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!isUpdatingToAmount && !s.isNullOrEmpty()) {
                    isUpdatingFromAmount = true
                    convertCurrency(true)
                    isUpdatingFromAmount = false
                } else if (s.isNullOrEmpty()) {
                    binding.editTextToAmount.setText("")
                }
            }
        })

        binding.editTextToAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!isUpdatingFromAmount && !s.isNullOrEmpty()) {
                    isUpdatingToAmount = true
                    convertCurrency(false)
                    isUpdatingToAmount = false
                } else if (s.isNullOrEmpty()) {
                    binding.editTextFromAmount.setText("")
                }
            }
        })


        binding.spinnerFromCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (!binding.editTextFromAmount.text.isNullOrEmpty()) {
                    convertCurrency(true)
                }
                updateExchangeRateText()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        binding.spinnerToCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (!binding.editTextFromAmount.text.isNullOrEmpty()) {
                    convertCurrency(true)
                }
                updateExchangeRateText()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        binding.imageViewSwap.setOnClickListener {
            swapCurrencies()
        }
    }

    private fun convertCurrency(fromSourceToTarget: Boolean) {
        try {
            val fromPosition = binding.spinnerFromCurrency.selectedItemPosition
            val toPosition = binding.spinnerToCurrency.selectedItemPosition

            val fromCurrency = currencyCodes[fromPosition]
            val toCurrency = currencyCodes[toPosition]

            if (fromSourceToTarget) {
                val amount = binding.editTextFromAmount.text.toString().toDoubleOrNull() ?: 0.0
                val result = convertAmount(amount, fromCurrency, toCurrency)
                binding.editTextToAmount.setText(formatAmount(result))
            } else {
                val amount = binding.editTextToAmount.text.toString().toDoubleOrNull() ?: 0.0
                val result = convertAmount(amount, toCurrency, fromCurrency)
                binding.editTextFromAmount.setText(formatAmount(result))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun convertAmount(amount: Double, fromCurrency: String, toCurrency: String): Double {

        val amountInUSD = amount / (exchangeRates[fromCurrency] ?: 1.0)


        return amountInUSD * (exchangeRates[toCurrency] ?: 1.0)
    }

    private fun formatAmount(amount: Double): String {
        return if (amount >= 1000) {
            String.format("%.2f", amount)
        } else {
            String.format("%.4f", amount)
        }
    }

    private fun swapCurrencies() {

        val fromPosition = binding.spinnerFromCurrency.selectedItemPosition
        val toPosition = binding.spinnerToCurrency.selectedItemPosition

        binding.spinnerFromCurrency.setSelection(toPosition)
        binding.spinnerToCurrency.setSelection(fromPosition)


        val fromAmount = binding.editTextFromAmount.text.toString()
        val toAmount = binding.editTextToAmount.text.toString()

        binding.editTextFromAmount.setText(toAmount)
        binding.editTextToAmount.setText(fromAmount)
    }

    private fun updateExchangeRateText() {
        val fromPosition = binding.spinnerFromCurrency.selectedItemPosition
        val toPosition = binding.spinnerToCurrency.selectedItemPosition

        val fromCurrency = currencyCodes[fromPosition]
        val toCurrency = currencyCodes[toPosition]

        val rate = convertAmount(1.0, fromCurrency, toCurrency)
        binding.textViewExchangeRate.text = String.format(
            "Tỷ giá: 1 %s = %s %s",
            fromCurrency,
            formatAmount(rate),
            toCurrency
        )
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}