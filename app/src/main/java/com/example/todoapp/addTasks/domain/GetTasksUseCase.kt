package com.example.todoapp.addTasks.domain

import com.example.todoapp.addTasks.data.TaskRepository
import com.example.todoapp.addTasks.ui.model.TasksModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(): Flow<List<TasksModel>> = taskRepository.tasks
}