package com.project.to_do_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.to_do_app.MainActivity
import com.project.to_do_app.R
import com.project.to_do_app.database.Data
import kotlinx.android.synthetic.main.view.view.*

class DataAdapter(var list:List<Data>, private val onClick: MainActivity) :
    RecyclerView.Adapter<DataAdapter.viewHolder>() {
    class viewHolder(view : View, onclick : noteClickListener ) : RecyclerView.ViewHolder(view) {

        var title:TextView = view.findViewById(R.id.eventEntered)
        var desc:TextView = view.findViewById(R.id.desc)
        var date:TextView = view.findViewById(R.id.date)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.view,parent,false)
        return viewHolder(view,onClick)

    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        val data:Data = list[position]
        holder.title.text = data.title
        holder.desc.text = data.desc
        holder.date.text = data.date

    }

    override fun getItemCount(): Int {

        return list.size

    }

    interface noteClickListener {

        fun onItemClick(position: Int)
    }

    fun clearList() {

        list = emptyList()
        notifyDataSetChanged()
    }
}