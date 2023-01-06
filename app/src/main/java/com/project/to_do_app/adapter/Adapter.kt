package com.project.to_do_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.to_do_app.R


class Adapter(var data : List<TaskInfo>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val task : TextView = itemView.findViewById(R.id.eventEntered)
        val due : TextView = itemView.findViewById(R.id.duedate)
        val layout : LinearLayout = itemView.findViewById(R.id.myLayout)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.view, parent)
        
        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.task.text = data[position].task
        holder.due.text = data[position].due

    }

    override fun getItemCount(): Int {

        return data.size

    }
}