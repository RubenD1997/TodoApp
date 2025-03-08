package com.example.todoapp.addTasks.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoapp.addTasks.ui.model.TasksModel
import java.util.UUID

@Entity(tableName = "task_table")
data class TaskEntity(
    @PrimaryKey
    val id: UUID,
    var title: String,
    var selected: Boolean
)

fun TaskEntity.toModel(): TasksModel {
    return TasksModel(
        id = id,
        task = title,
        selected = selected
    )
}