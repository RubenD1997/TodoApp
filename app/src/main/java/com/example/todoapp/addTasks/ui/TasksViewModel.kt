package com.example.todoapp.addTasks.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.addTasks.domain.AddTaskUseCase
import com.example.todoapp.addTasks.domain.DeleteTaskUseCase
import com.example.todoapp.addTasks.domain.GetTasksUseCase
import com.example.todoapp.addTasks.domain.UpdateTaskUseCase
import com.example.todoapp.addTasks.ui.model.TasksModel
import com.example.todoapp.core.Resource
import com.example.todoapp.core.Resource.Failure
import com.example.todoapp.core.Resource.Loading
import com.example.todoapp.core.Resource.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase
) : ViewModel() {

    val uiState: StateFlow<Resource<List<TasksModel>>> = getTasksUseCase()
        .map { Success(it) }
        .catch { Failure(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading())


    private val _showDialog = MutableLiveData<Boolean>()
    var showDialog: LiveData<Boolean> = _showDialog

    fun onDialogClose() {
        _showDialog.value = false
    }

    fun onTaskCreate(task: String) {
        Log.i("TAG", "onTaskCreate: $task")
        viewModelScope.launch {
            addTaskUseCase(TasksModel(task = task))
        }
        onDialogClose()
    }

    fun onDialogShow() {
        _showDialog.value = true
    }

    fun onItemRemove(tasksModel: TasksModel) {
        viewModelScope.launch {
            deleteTaskUseCase(tasksModel)
        }
    }

    fun onCheckBoxSelected(tasksModel: TasksModel) {
        viewModelScope.launch {
            updateTaskUseCase(tasksModel.copy(selected = !tasksModel.selected))
        }
    }
}