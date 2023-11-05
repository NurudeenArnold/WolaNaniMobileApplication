package com.example.WolaNani

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DonatorAdapter(private val dataList: List<DonatorData>) : RecyclerView.Adapter<DonatorAdapter.ViewHolder>() {

    class ViewHolder(donators_list : View) : RecyclerView.ViewHolder(donators_list) {

        val item = donators_list.findViewById<TextView>(R.id.item)
        val reference = donators_list.findViewById<TextView>(R.id.reference)
        val phoneNumber = donators_list.findViewById<TextView>(R.id.phoneNumber)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.donators_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]

        holder.item.text = data.item
        holder.reference.text = data.reference
        holder.phoneNumber.text = data.phoneNumber
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
