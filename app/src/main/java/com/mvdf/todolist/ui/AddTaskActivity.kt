package com.mvdf.todolist.ui

import android.app.Activity
import android.os.Bundle
import android.text.format.DateFormat
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.mvdf.todolist.databinding.ActivityAddTaskBinding
import com.mvdf.todolist.datasource.TaskDataSource
import com.mvdf.todolist.extensions.format
import com.mvdf.todolist.extensions.text
import com.mvdf.todolist.model.Task
import java.util.*

class AddTaskActivity: AppCompatActivity() {

    companion object{
        const val TASK_ID = "task_id"
    }

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra(TASK_ID)){
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.tilTitle.text = it.title
                binding.tilDescription.text = it.description
                binding.tilDate.text = it.date
                binding.tilHour.text = it.hour
            }
        }

        insertListeners()
    }

    private fun insertListeners() {
        binding.tilDate.editText?.setOnClickListener{
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.tilDate.text = Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager,"DATE_PICKER_TAG")
        }

        binding.tilHour.editText?.setOnClickListener{
            val is24Hour = DateFormat.is24HourFormat(this)
            val clockFormat = if (is24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(clockFormat).build()

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
            val task = Task(
                title = binding.tilTitle.text,
                description = binding.tilDescription.text,
                date = binding.tilDate.text,
                hour = binding.tilHour.text,
                id = intent.getIntExtra(TASK_ID,0)
            )
            TaskDataSource.insertTask(task)
            setResult(Activity.RESULT_OK)
            finish()
        }

    }

}