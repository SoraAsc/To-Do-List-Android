package com.mvdf.todolist.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mvdf.todolist.entity.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskRoomDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDAO

    companion object{
        @Volatile
        private var INSTANCE: TaskRoomDatabase? = null

        fun getDatabase(ctx: Context, scope: CoroutineScope): TaskRoomDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    ctx.applicationContext,
                    TaskRoomDatabase::class.java,
                    "task_database"
                ).addCallback(TaskDatabaseCallBack(scope)).build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class TaskDatabaseCallBack(private val scope: CoroutineScope) : RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database -> scope.launch { populateDatabase(database.taskDao()) } }
        }

        suspend fun populateDatabase(taskDao: TaskDAO) {
            taskDao.deleteAll()
        }
    }

}