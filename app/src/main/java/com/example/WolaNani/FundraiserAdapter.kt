package com.example.WolaNani

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FundraiserAdapter(private val dataList: MutableList<FundraiserData> = mutableListOf()) : RecyclerView.Adapter<FundraiserAdapter.ViewHolder>() {

    val databaseref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://wolanani-53ae1-default-rtdb.firebaseio.com");
    class ViewHolder(fundraisers_list : View) : RecyclerView.ViewHolder(fundraisers_list) {

        val name = fundraisers_list.findViewById<TextView>(R.id.item)
        val description = fundraisers_list.findViewById<TextView>(R.id.description)
        val goal = fundraisers_list.findViewById<TextView>(R.id.goal)
        val progressBar = fundraisers_list.findViewById<ProgressBar>(R.id.progressBar)
        val progress = fundraisers_list.findViewById<TextView>(R.id.cateProgress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fundraisers_list, parent, false)
        return ViewHolder(view)
    }
    private fun calculateProgress(currentCount: Int, goal: Int): Int {
        if (goal <= 0) {
            return 0
        }
        val progress = (currentCount.toFloat() / goal.toFloat() * 100).toInt()

        return if (progress < 0) 0 else if (progress > 100) 100 else progress
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]

        holder.name.text = data.name
        holder.description.text = data.description
        holder.goal.text = data.goal.toString()

        databaseref.child("fundraisers").child(data.name).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val goal = dataSnapshot.child("goal").getValue(Int::class.java) ?: 0
                val total = dataSnapshot.child("total").getValue(Int::class.java) ?: 0

                Log.d("Goal (String)", goal.toString())
                Log.d("Total (String)", total.toString())

                val progress = calculateProgress(total, goal)

                holder.progressBar.max = 100
                holder.progressBar.progress = progress

                holder.progress.text = "$progress%"

                Log.d("Fundraiser name", data.name)
                Log.d("Progress", progress.toString())
                Log.d("Goal", goal.toString())
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(newDataList: List<FundraiserData>) {
        dataList.clear()
        dataList.addAll(newDataList)
        notifyDataSetChanged()
    }
}
