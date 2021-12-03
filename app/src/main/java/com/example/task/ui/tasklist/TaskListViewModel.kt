package com.example.task.ui.tasklist

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.task.R
import com.example.task.data.repository.TaskRepository
import com.example.task.model.Task
import kotlinx.android.synthetic.main.task_dialog.view.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class TaskListViewModel constructor(private val repository: TaskRepository) : ViewModel() {
    val TAG: String = "TaskListViewModel"
    var taskListLiveData = MutableLiveData<List<Task>>()

    init {
        loadTaskList()
    }


    private fun loadTaskList() {
        viewModelScope.launch {
            try {

                Log.d(TAG, "viewModelScope.launch ")
                val listNotLiveData = repository.getTaskList()

                // メンバー変数のLiveDataにこの値を送っている　これが大事！！ここは、LiveDataを送るのではなく、リスト形式を送るのがミソ
                taskListLiveData.postValue(listNotLiveData)


            } catch (e: Exception) {

                Log.e(TAG, "データ取得中にエラー " + e)

            }
        }
    }

    fun onButtonClicked(context: Context) {
        var task_array = ArrayList<Task>()

        val infView = View.inflate(context,R.layout.task_dialog,null)

        AlertDialog.Builder(context)
            .setTitle(R.string.dialog_title)
            .setView(infView)
            .setPositiveButton(R.string.dialog_add, { dialog, which ->
                var taskName = infView.inputName.text.toString()
                var taskDeadline = infView.inputDeadline.text.toString()
                var taskRequired = infView.inputRequired.text.toString()
                val task = Task(null, taskName, taskDeadline, taskRequired, "2020/02/30")
                task_array.add(task)
                viewModelScope.launch {
                    val index = Random().nextInt(task_array.size) // ランダムに選択された 0 以上 4 未満の整数
                    val result = task_array.get(index)
                    addTaskDb(context, result)

                    //タスクリストをリフレッシュして表示するのに必要
                    loadTaskList()
                }
            })
            .setNegativeButton(R.string.dialog_cancel, null)
            .show()
    }


    suspend fun addTaskDb(context: Context, task: Task) {
        try {

            repository.addTask(context, task)

        } catch (cause: Throwable) {
            // If anything throws an exception, inform the caller
            Log.e("TaskViewModel", "エラー　でーたべーす")
            Toast.makeText(context, "データベースエラーで更新できませんでした!", Toast.LENGTH_LONG).show();
        }
    }



    //引数が必要な時は、Factoryが必要
    class Factory(private val repository: TaskRepository) :
        ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TaskListViewModel(repository) as T
        }
    }
}