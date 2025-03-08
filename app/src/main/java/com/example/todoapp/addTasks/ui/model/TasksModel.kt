package com.example.todoapp.addTasks.ui.model

import com.example.todoapp.addTasks.data.TaskEntity
import java.util.UUID

data class TasksModel(
    val id: UUID = UUID.randomUUID(),
    val task: String,
    var selected: Boolean = false
)

fun TasksModel.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = task,
        selected = selected
    )
}