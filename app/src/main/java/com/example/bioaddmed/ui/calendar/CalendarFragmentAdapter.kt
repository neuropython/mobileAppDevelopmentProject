package com.example.bioaddmed.ui.calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bioaddmed.R

class CalendarFragmentAdapter(private val eventsList: ArrayList<EventsWithoutDesc>)
    : RecyclerView.Adapter<CalendarFragmentAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            MyViewHolder {
        val itemView =  LayoutInflater.from(parent.context).
        inflate(R.layout.list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return eventsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentEvent = eventsList[position]
        holder.date.text = currentEvent.date
        holder.summary.text = currentEvent.summary
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val date = itemView.findViewById<TextView>(R.id.date)
        val summary = itemView.findViewById<TextView>(R.id.summary)
    }
}