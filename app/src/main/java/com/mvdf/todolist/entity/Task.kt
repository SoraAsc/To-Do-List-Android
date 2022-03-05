package com.mvdf.todolist.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "hour") val hour: String,
)
/*data class Task(

    val title: String,
    val description: String,
    val date: String,
    val hour: String,
    val id: Int = 0
){

    override fun equals(other: Any?): Boolean {
        if(this===other) return true
        if(javaClass != other?.javaClass) return false

        other as Task

        if(id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }

}*/
