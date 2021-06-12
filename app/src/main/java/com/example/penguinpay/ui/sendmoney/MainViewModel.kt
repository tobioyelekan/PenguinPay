package com.example.penguinpay.ui.sendmoney

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.penguinpay.data.helper.Resource
import com.example.penguinpay.data.model.Country
import com.example.penguinpay.data.source.repo.GetRatesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(private val repo: GetRatesRepo) : ViewModel() {

    private val _selectedCountry = MutableLiveData<Country>()
    val selectedCountry: LiveData<Country> = _selectedCountry

    private val _amountInBinary = MutableLiveData<String>()

    val selectedCountryString: Country?
        get() = selectedCountry.value

    init {
        setSelectedCountry(Country("KES", "Kenya", "+254", 9))
    }

    fun setSelectedCountry(country: Country) {
        _selectedCountry.value = country
    }

    fun convertMoney(amountInBinary: String) {
        _amountInBinary.value = amountInBinary
    }

    //simulate loading
    fun sendMoney() = liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
        emit(true)
        delay(3000)
        emit(false)
    }

    val convertToLocalCurrency: LiveData<Resource<String>> = _amountInBinary.switchMap {
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            try {
                emit(Resource.loading(null))
                val response = repo.getLocalCurrencyRate(it, selectedCountry.value?.code ?: "")
                emit(Resource.success(response))
            } catch (ex: Throwable) {
                ex.printStackTrace()
                emit(Resource.error(ex.message!!, null))
            }
        }
    }
}