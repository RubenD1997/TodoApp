package com.example.todoapp.addTasks.ui

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoapp.addTasks.ui.model.TasksModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor() : ViewModel() {

    private val _showDialog = MutableLiveData<Boolean>()
    var showDialog: LiveData<Boolean> = _showDialog

    private val _tasks = mutableStateListOf<TasksModel>()
    val tasks: List<TasksModel> = _tasks

    fun onDialogClose() {
        _showDialog.value = false
    }

    fun onTaskCreate(task: String) {
        Log.i("TAG", "onTaskCreate: $task")
        _tasks.add(TasksModel(task = task))
        onDialogClose()
    }

    fun onDialogShow() {
        _showDialog.value = true
    }

    fun onItemRemove(tasksModel: TasksModel) {
        val task = _tasks.find { it.id == tasksModel.id }
        _tasks.remove(task)
    }

    fun onCheckBoxSelected(tasksModel: TasksModel) {
        val index = _tasks.indexOf(tasksModel)
        _tasks[index] = _tasks[index].let {
            it.copy(selected = !it.selected)
        }

    }
}