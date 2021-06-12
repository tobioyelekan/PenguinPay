package com.example.penguinpay.ui.sendmoney.selectcountry

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.penguinpay.R
import com.example.penguinpay.data.model.Country
import com.example.penguinpay.util.loadImage
import kotlinx.android.synthetic.main.item_country.view.*

class CountryAdapter(val onItemClick: ((Country) -> Unit)) :
    ListAdapter<Country, CountryAdapter.ViewHolder>(CountryDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_country, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(country: Country) {
            with(itemView) {
                countryImg.loadImage(country.code)
                countryName.text = country.name
                setOnClickListener { onItemClick(country) }
            }
        }
    }
}

class CountryDiffCallback : DiffUtil.ItemCallback<Country>() {
    override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem == newItem
    }
}