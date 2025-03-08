package com.example.todoapp.addTasks.domain

import com.example.todoapp.addTasks.data.TaskRepository
import com.example.todoapp.addTasks.ui.model.TasksModel
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(task: TasksModel) {
        taskRepository.addTask(task)
    }

}