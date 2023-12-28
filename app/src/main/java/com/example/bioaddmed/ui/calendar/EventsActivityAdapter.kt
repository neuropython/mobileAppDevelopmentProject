package com.example.bioaddmed.ui.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bioaddmed.R

class EventsActivityAdapter(private val eventsList: List<EventsWithDesc>)
    : RecyclerView.Adapter<EventsActivityAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            MyViewHolder {
        val itemView =  LayoutInflater.from(parent.context).
        inflate(R.layout.list_with_desc_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return eventsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentEvent = eventsList[position]
        holder.name.text = currentEvent.name
        holder.time.text = currentEvent.startTime
        holder.date.text = currentEvent.date
        holder.description.text = currentEvent.description
        holder.author.text = currentEvent.author
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val date: TextView = itemView.findViewById<TextView>(R.id.dayView)
        val name: TextView = itemView.findViewById<TextView>(R.id.nameView)
        val time: TextView = itemView.findViewById<TextView>(R.id.timeView)
        val description: TextView = itemView.findViewById<TextView>(R.id.descView)
        val author: TextView = itemView.findViewById<TextView>(R.id.authorView)

    }
}