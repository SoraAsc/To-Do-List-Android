package com.mvdf.todolist.ui

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import com.mvdf.todolist.R
import com.mvdf.todolist.databinding.ActivityMainBinding
import com.mvdf.todolist.datasource.TaskDataSource

class MainActivity : AppCompatActivity() {

    companion object{
        private const val CREATE_NEW_TASK = 1000
    }

    private lateinit var binding : ActivityMainBinding
    private val taskAdapter by lazy { TaskListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvMainTask.adapter = taskAdapter
        updateList()

        insertListener()

        val appSettingsPrefs: SharedPreferences = getSharedPreferences("AppSettingsPrefs",0)
        val sharedPrefsEdit: SharedPreferences.Editor = appSettingsPrefs.edit()
        val isNightModeOn: Boolean = appSettingsPrefs.getBoolean("NightMode", false)

        if(isNightModeOn)
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.imgBtnChangeMode.setImageResource(R.drawable.ic_sun)
        }
        else
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.imgBtnChangeMode.setImageResource(R.drawable.ic_moon)
        }

        binding.imgBtnChangeMode.setOnClickListener {
            if(isNightModeOn)
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.imgBtnChangeMode.setImageResource(R.drawable.ic_moon)
                sharedPrefsEdit.putBoolean("NightMode", false)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.imgBtnChangeMode.setImageResource(R.drawable.ic_sun)
                sharedPrefsEdit.putBoolean("NightMode", true)
            }
            sharedPrefsEdit.apply()
        }
    }

    private fun insertListener() {
        binding.fabCreateTask.setOnClickListener{
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivityForResult(intent, CREATE_NEW_TASK)
        }

        taskAdapter.listenerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)
        }

        taskAdapter.listenerDelete = {
            TaskDataSource.deleteTask(it)
            updateList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK){
            updateList()
        }
    }

    private fun updateList(){
        val list = TaskDataSource.getList()

        binding.includeEmptyTaskState.emptyTaskState.visibility =
            if (list.isEmpty()) View.VISIBLE else View.GONE

        taskAdapter.submitList(list)
    }


}