package com.example.todoapp.addTasks.data

import com.example.todoapp.addTasks.ui.model.TasksModel
import com.example.todoapp.addTasks.ui.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    val tasks: Flow<List<TasksModel>> = taskDao.getAllTasks().map {
        it.map { entity -> entity.toModel() }
    }

    suspend fun addTask(task: TasksModel) {
        taskDao.saveTask(task.toEntity())
    }

    suspend fun deleteTask(task: TasksModel) {
        taskDao.deleteTask(task.toEntity())
    }

    suspend fun updateTask(task: TasksModel) {
        taskDao.updateTask(task.toEntity())

    }
}