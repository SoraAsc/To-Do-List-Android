package com.mvdf.todolist.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.mvdf.todolist.datasource.TaskRepository
import com.mvdf.todolist.datasource.TaskRoomDatabase
import com.mvdf.todolist.entity.Task
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application){

    val allTasks: LiveData<List<Task>>
    private val repository: TaskRepository

    init {
        val taskDao = TaskRoomDatabase.getDatabase(application,viewModelScope).taskDao()
        repository = TaskRepository(taskDao)
        allTasks = repository.allTasks.asLiveData()
    }

    fun insert(task: Task) = viewModelScope.launch { repository.insert(task) }
    fun delete(task: Task) = viewModelScope.launch { repository.delete(task) }
}

