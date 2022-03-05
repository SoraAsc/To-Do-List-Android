package com.mvdf.todolist.datasource

import com.mvdf.todolist.entity.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDAO) {

    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun insert(task: Task){
        taskDao.insert(task)
    }

    suspend fun update(task: Task){
        taskDao.update(task)
    }

    suspend fun delete(task: Task){
        taskDao.delete(task)
    }

}