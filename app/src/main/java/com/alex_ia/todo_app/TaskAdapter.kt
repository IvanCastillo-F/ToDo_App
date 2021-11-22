package com.alex_ia.todo_app

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.checkbox.MaterialCheckBox
import java.time.format.DateTimeFormatter

class TaskAdapter(
    val list: MutableList<Task>,
    var OnClickDoneTask:(task:Task,position:Int)->Unit,
    var OnClickDetailTask:(task:Task)->Unit
    ):
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){

    fun add(task:Task){
        list.add(task)
        notifyItemInserted(list.size - 1)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeTask(position: Int){
        list.removeAt(position)

        notifyDataSetChanged()
    }

    fun update(task:Task){
        val index = list.indexOfFirst { it.id == task.id }
        list[index] = task

        notifyItemChanged(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.TaskViewHolder {
       return TaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.task_row, parent, false))
    }

    override fun onBindViewHolder(holder: TaskAdapter.TaskViewHolder, position: Int) {
        holder.bind(list[position], holder.adapterPosition)
    }

    override fun getItemCount() = list.size


    inner class TaskViewHolder(private val view:View):RecyclerView.ViewHolder(view){
        @SuppressLint("NotifyDataSetChanged")
        fun bind(data:Task, position: Int) = view.apply {

            val txvTitle = findViewById<TextView>(R.id.txvTitle)
            val txvDatetime = findViewById<TextView>(R.id.txvDateTime)
            val chkFinished: MaterialCheckBox = findViewById(R.id.chkFinished)

            txvTitle.text = data.title
            txvDatetime.text = data.dateTime?.format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm a"))

            if (chkFinished.isChecked){
                chkFinished.isChecked = false
                chkFinished.isSelected = false
            }

            chkFinished.setOnClickListener{
              OnClickDoneTask(data,position)
            }

            rootView.setOnClickListener {
                OnClickDetailTask(data)
            }
        }
    }
}