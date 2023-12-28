package com.example.bioaddmed.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bioaddmed.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class UserAdapter (private var usersList : List<UserData>, private val listener: UserAdapterListener)
    : RecyclerView.Adapter<UserAdapter.MyViewHolder>(){

    interface UserAdapterListener {
        fun onChipPressed(user: UserData)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =  LayoutInflater.from(parent.context)
            .inflate(R.layout.user_to_choose, parent, false) as ChipGroup

        return MyViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: UserAdapter.MyViewHolder, position: Int) {
        val currentUser = usersList[position]
        holder.userName.text = currentUser.name
        holder.userName.setOnClickListener {
            holder.userName.isChecked = !holder.userName.isChecked
            listener.onChipPressed(currentUser)
        }
    }

    override fun getItemCount(): Int {
        return usersList.size
    }
    class MyViewHolder(itemView: ChipGroup) : RecyclerView.ViewHolder(itemView) {
        val userName: Chip = itemView.findViewById<Chip>(R.id.chip)
    }
    }