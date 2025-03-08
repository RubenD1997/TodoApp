package com.example.todoapp.addTasks.data.di

import android.content.Context
import androidx.room.Room
import com.example.todoapp.addTasks.data.TaskDao
import com.example.todoapp.addTasks.data.TodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideTaskDao(todoDatabase: TodoDatabase): TaskDao = todoDatabase.taskDao()

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): TodoDatabase {
        return Room.databaseBuilder(context, TodoDatabase::class.java, "tasksDB").build()
    }
}