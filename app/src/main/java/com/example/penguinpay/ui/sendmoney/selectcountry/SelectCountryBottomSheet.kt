package com.example.penguinpay.ui.sendmoney.selectcountry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.penguinpay.R
import com.example.penguinpay.data.model.Country
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_select_currency.*

class SelectCountryBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_currency, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dummyCountries = listOf(
            Country("KES", "Kenya", "+254", 9),
            Country("NGN", "Nigeria", "+234", 7),
            Country("TZS", "Tanzania", "+255", 9),
            Country("UGX", "Uganda", "+256", 7)
        )

        val adapter = CountryAdapter {
            val dialogListener = requireActivity() as DialogListener
            dialogListener.onSelect(it)
            dismiss()
        }

        countryList.adapter = adapter
        adapter.submitList(dummyCountries)

    }

    interface DialogListener {
        fun onSelect(country: Country)
    }
}