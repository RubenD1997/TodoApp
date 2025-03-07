package com.example.todoapp.addTasks.ui.model

import java.util.UUID

data class TasksModel(
    val id: UUID = UUID.randomUUID(),
    val task: String,
    var selected: Boolean = false
)