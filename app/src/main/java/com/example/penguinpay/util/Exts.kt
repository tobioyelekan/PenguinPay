package com.example.penguinpay.util

import android.app.Activity
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.penguinpay.R
import java.util.*

const val FLAG_CDN_URL = "https://flagcdn.com/28x21/"

fun Fragment.showMessage(msg: String) {
    Toast.makeText(this.requireContext(), msg, Toast.LENGTH_SHORT).show()
}

fun ImageView.loadImage(name: String) {
    if (name.isNotEmpty()) {
        val requestOptions = RequestOptions()
            .placeholder(R.drawable.ic_default_currency)
            .error(R.drawable.ic_default_currency)

        Glide.with(context)
            .setDefaultRequestOptions(requestOptions)
            .load(getImageUrl(name))
            .into(this)
    }
}

fun Activity.showMsg(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun EditText.text() = this.text.toString()

fun getImageUrl(symbol: String): String {
    return if (symbol.length < 2) " "
    else "$FLAG_CDN_URL${symbol.substring(0, 2).toLowerCase(Locale.getDefault())}.png"
}