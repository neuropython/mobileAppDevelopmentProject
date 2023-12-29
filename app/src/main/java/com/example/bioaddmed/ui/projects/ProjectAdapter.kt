package com.example.bioaddmed.ui.projects

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bioaddmed.R

class ProjectAdapter (private val projectsList : List<ProjectData>)
    : RecyclerView.Adapter<ProjectAdapter.MyViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =  LayoutInflater.from(parent.context)
            .inflate(R.layout.project_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return projectsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentProject = projectsList[position]
        holder.name!!.text = currentProject.name
        holder.people!!.text = currentProject.people.toString()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView? = itemView.findViewById(R.id.projectNameTextView)
        val people: TextView? = itemView.findViewById(R.id.peopleTextView)
    }
}