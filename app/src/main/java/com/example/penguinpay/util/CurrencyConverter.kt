package com.example.penguinpay.util

class CurrencyConverter(private var amount: String, private val rate: Double = 0.0) {

    fun getConvertedRate(): String {
        if (!isAmountBinary()) throw Throwable("amount is not a valid binaria format!")
        val decimal = toDecimal(amount)
        val preRate = (decimal.toInt() * rate).toInt()

        return toBinary(preRate)
    }

    fun isAmountBinary() = amount.matches(Regex("[01]+")) && amount != "0"

    private fun toDecimal(amount: String): String {
        return Integer.parseInt(amount, 2).toString()
    }

    private fun toBinary(amount: Int): String {
        return Integer.toBinaryString(amount)
    }
}