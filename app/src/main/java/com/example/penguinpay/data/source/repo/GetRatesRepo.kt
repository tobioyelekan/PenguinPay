package com.example.penguinpay.data.source.repo

import com.example.penguinpay.data.source.remote.RateService
import com.example.penguinpay.util.CurrencyConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import javax.inject.Inject

class GetRatesRepo @Inject constructor(private val rateService: RateService) {
    suspend fun getLocalCurrencyRate(amountInBinary: String, currency: String) =
        withContext(Dispatchers.IO) {
            val result = rateService.getCurrencyRates()

            //convert rates
            val rate = getCurrencyRate(result, currency)
            val currencyConverter = CurrencyConverter(amountInBinary, rate)

            currencyConverter.getConvertedRate()
        }

    private fun getCurrencyRate(result: String, currency: String): Double {
        try {
            val jsonObjectRates = JSONObject(result).getJSONObject("rates")

            if (jsonObjectRates.has(currency.toUpperCase(Locale.getDefault())))
                return jsonObjectRates.getDouble(currency)
            else {
                throw Throwable(message = "currency not valid or does not exist")
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return 0.0
    }

}