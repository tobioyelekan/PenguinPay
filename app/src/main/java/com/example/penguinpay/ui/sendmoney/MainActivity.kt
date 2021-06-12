package com.example.penguinpay.ui.sendmoney

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.penguinpay.R
import com.example.penguinpay.data.helper.Resource.Status
import com.example.penguinpay.data.model.Country
import com.example.penguinpay.ui.sendmoney.selectcountry.SelectCountryBottomSheet
import com.example.penguinpay.util.CurrencyConverter
import com.example.penguinpay.util.loadImage
import com.example.penguinpay.util.showMsg
import com.example.penguinpay.util.text
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SelectCountryBottomSheet.DialogListener {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectPhone.setOnClickListener {
            SelectCountryBottomSheet().show(supportFragmentManager, "currency")
        }

        sendMoney.setOnClickListener {
            if (validate()) {
                viewModel.sendMoney().observe(this, Observer {
                    when (it) {
                        true -> {
                            sendMoney.isEnabled = false
                            sendMoney.text = ""
                            progress.visibility = View.VISIBLE
                        }
                        else -> {
                            sendMoney.text = getString(R.string.label_send)
                            sendMoney.isEnabled = true
                            progress.visibility = View.GONE

                            showMsg("Money sent successfully")
                            resetFields()
                        }
                    }
                })
            }
        }

        amount.addTextChangedListener(amountTextWatcher)

        setupObserver()
    }

    private fun resetFields() {
        fname.text = null
        lname.text = null
        phone.text = null
        amount.text = null
    }

    private val amountTextWatcher = object : TextWatcher {
        val handler: Handler = Handler(Looper.getMainLooper())

        override fun afterTextChanged(s: Editable?) {
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed(userStoppedTyping, 1000)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.isEmpty()) receiveAmount.visibility = View.GONE
        }

        var userStoppedTyping = Runnable {
            if (amount.text().isNotEmpty()) {
                viewModel.convertMoney(amount.text())
            }
        }

    }

    private fun validate(): Boolean {
        var isValid = true

        if (fname.text().isEmpty()) {
            fname.error = "First name cannot be empty"
            isValid = false
        }

        if (lname.text().isEmpty()) {
            lname.error = "Last name cannot be empty"
            isValid = false
        }

        if (phone.text().isEmpty()) {
            phone.error = "Mobile number cannot be empty"
            isValid = false
        } else {
            viewModel.selectedCountryString?.let { country ->
                if (phone.text().length != country.suffixLength) {
                    phone.error = "Invalid mobile number"
                    isValid = false
                }
            }
        }

        if (amount.text().isEmpty()) {
            amount.error = "Amount cannot be empty"
            isValid = false
        } else {
            val currencyConverter = CurrencyConverter(amount.text())
            if (!currencyConverter.isAmountBinary()) {
                amount.error = "Amount must be binaria format"
                isValid = false
            }
        }

        return isValid

    }

    private fun setupObserver() {
        viewModel.selectedCountry.observe(this, Observer {
            selectedImg.loadImage(it.code)
            countryPrefix.text = it.prefix
            phone.filters = arrayOf(InputFilter.LengthFilter(it.suffixLength))
        })

        viewModel.convertToLocalCurrency.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> loading.visibility = View.VISIBLE
                Status.SUCCESS -> {
                    loading.visibility = View.GONE
                    it.data?.let { localAmount ->
                        receiveAmount.text = getString(R.string.label_recipient, localAmount)
                        receiveAmount.visibility = View.VISIBLE
                    }
                }
                Status.ERROR -> {
                    loading.visibility = View.GONE
                    showMsg(it.message!!)
                }
            }
        })
    }

    override fun onSelect(country: Country) {
        viewModel.setSelectedCountry(country)
    }

}