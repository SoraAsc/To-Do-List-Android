package com.mvdf.todolist.ui

import android.os.Bundle
import android.text.format.DateFormat
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.mvdf.todolist.R
import com.mvdf.todolist.databinding.ActivityAddTaskBinding
import com.mvdf.todolist.extensions.format
import com.mvdf.todolist.extensions.text
import com.mvdf.todolist.entity.Task
import com.mvdf.todolist.viewmodel.TaskViewModel
import java.util.*

class AddTaskActivity: AppCompatActivity() {

    companion object{
        const val TASK_ID = "task_id"
        const val TASK_TITLE = "task_title"
        const val TASK_DESCRIPTION = "task_description"
        const val TASK_DATE = "task_date"
        const val TASK_HOUR = "task_hour"
    }
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        if(intent.hasExtra(TASK_ID)){
            binding.toolbar.title = getString(R.string.edit_task_text)
            binding.btnCreateNewTask.text = getString(R.string.edit_task_text)
            binding.tilTitle.text = intent.getStringExtra(TASK_TITLE).toString()
            binding.tilDescription.text = intent.getStringExtra(TASK_DESCRIPTION).toString()
            binding.tilDate.text = intent.getStringExtra(TASK_DATE).toString()
            binding.tilHour.text = intent.getStringExtra(TASK_HOUR).toString()
        }
        insertListeners()
    }

    private fun insertListeners() {

        binding.toolbar.setNavigationOnClickListener{
            finish()
        }

        binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.tilDate.text = Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.tilHour.editText?.setOnClickListener{
            val is24Hour = DateFormat.is24HourFormat(this)
            val clockFormat = if (is24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

            val timePicker = MaterialTimePicker.Builder().setTimeFormat(clockFormat).build()

            timePicker.addOnPositiveButtonClickListener{
                val hour = if (timePicker.hour<=9) "0${timePicker.hour}" else "${timePicker.hour}"
                val minute = if (timePicker.minute<=9) "0${timePicker.minute}" else "${timePicker.minute}"
                binding.tilHour.text = "${hour}:${minute}"
            }
            timePicker.show(supportFragmentManager, "HOUR_PICKER_TAG")
        }

        binding.btnCancel.setOnClickListener{
            finish()
        }

        binding.btnCreateNewTask.setOnClickListener{
            var task = Task(
                title = binding.tilTitle.text,
                description = binding.tilDescription.text,
                date = binding.tilDate.text,
                hour = binding.tilHour.text,
            )
            if(intent.hasExtra(TASK_ID)){
                task = task.copy(id = intent.getIntExtra(TASK_ID,-1))
            }
            if(task.id <= 0) taskViewModel.insert(task)
            else taskViewModel.update(task)


            finish()
        }

    }

}