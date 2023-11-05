package com.example.WolaNani

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VolunteerAdapter(private val dataList: List<VolunteerData>) : RecyclerView.Adapter<VolunteerAdapter.ViewHolder>() {

    class ViewHolder(volunteers_list : View) : RecyclerView.ViewHolder(volunteers_list) {

        val name = volunteers_list.findViewById<TextView>(R.id.item)
        val phoneNumber = volunteers_list.findViewById<TextView>(R.id.phoneNumber)
        val reason = volunteers_list.findViewById<TextView>(R.id.reason)
        val address = volunteers_list.findViewById<TextView>(R.id.address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.volunteers_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]

        holder.name.text = data.name
        holder.phoneNumber.text = data.phoneNumber
        holder.reason.text = data.reason
        holder.address.text = data.address
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
