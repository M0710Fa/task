package com.example.task.ui.tasklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.model.Task
import com.example.task.databinding.TaskItemBinding

class TaskListAdapter (private val taskClickCallback: TaskClickCallback?):RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

        private var taskList: List<Task?>? = null

        fun setTaskList(taskList: List<Task?>?) {

            this.taskList = taskList

            //これ大事。ないと、データ追加後に画面が更新されません。
            notifyDataSetChanged()

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): TaskViewHolder {
            val binding =
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.task_item, parent,
                    false
                ) as TaskItemBinding


            return TaskViewHolder(binding)
        }


        override fun getItemCount(): Int {
            return taskList?.size ?: 0
        }

        open class TaskViewHolder(val binding: TaskItemBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onBindViewHolder(holder: TaskListAdapter.TaskViewHolder, position: Int) {
            holder.binding.task = taskList?.get(position) //task_list_item.xmlの中のtask
            holder.binding.executePendingBindings()
        }
}