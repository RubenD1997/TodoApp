package com.example.todoapp.addTasks.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.produceState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.todoapp.addTasks.ui.model.TasksModel
import com.example.todoapp.core.Resource
import com.example.todoapp.core.Resource.Failure
import com.example.todoapp.core.Resource.Loading
import com.example.todoapp.core.Resource.Success

@Composable
fun TasksScreen(modifier: Modifier, tasksViewModel: TasksViewModel) {
    val showDialog: Boolean by tasksViewModel.showDialog.observeAsState(initial = false)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<Resource<List<TasksModel>>>(
        initialValue = Loading(),
        key1 = lifecycle,
        key2 = tasksViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            tasksViewModel.uiState.collect { value = it }
        }
    }
    Box(modifier = modifier.fillMaxSize()) {
        when (val result = uiState) {
            is Failure -> Text(
                "Error to loading ${result.exception.message}",
                modifier.align(Alignment.Center)
            )

            is Loading -> CircularProgressIndicator(modifier.align(Alignment.Center))
            is Success -> {
                AddTaskDialog(showDialog) { actonTask ->
                    when (actonTask) {
                        is ActionDialog.AddTask -> tasksViewModel.onTaskCreate(actonTask.task)
                        ActionDialog.Dismiss -> tasksViewModel.onDialogClose()
                    }
                    tasksViewModel.onDialogClose()
                }
                FabDialog(modifier = Modifier.align(Alignment.BottomEnd)) {
                    tasksViewModel.onDialogShow()
                }
                TasksList(result.data, tasksViewModel)
            }
        }
    }
}

@Composable
fun TasksList(tasks: List<TasksModel>, tasksViewModel: TasksViewModel) {
    LazyColumn {
        items(tasks, key = { it.id }) {
            ItemTask(it, tasksViewModel)
        }
    }
}

@Composable
fun ItemTask(tasksModel: TasksModel, tasksViewModel: TasksViewModel) {
    Card(
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    tasksViewModel.onItemRemove(tasksModel)
                })
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = tasksModel.task, modifier = Modifier.weight(1f))
            Checkbox(checked = tasksModel.selected, onCheckedChange = {
                tasksViewModel.onCheckBoxSelected(tasksModel)
            })
        }
    }
}

@Composable
fun FabDialog(modifier: Modifier, openDialog: () -> Unit) {
    FloatingActionButton(
        onClick = { openDialog() },
        modifier = modifier.padding(16.dp)
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
    }
}

sealed class ActionDialog() {
    data class AddTask(val task: String) : ActionDialog()
    data object Dismiss : ActionDialog()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(show: Boolean = false, onDialogAction: (ActionDialog) -> Unit) {
    var myTask by rememberSaveable { mutableStateOf("") }
    if (show) {
        BasicAlertDialog(onDismissRequest = { onDialogAction(ActionDialog.Dismiss) }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Añade tu tarea",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    TextField(
                        value = myTask,
                        onValueChange = { myTask = it },
                        singleLine = true,
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    Button(onClick = {
                        onDialogAction(ActionDialog.AddTask(myTask))
                        myTask = ""
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Añadir tarea")
                    }
                }
            }
        }
    }
}
