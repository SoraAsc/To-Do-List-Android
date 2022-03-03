package com.mvdf.todolist.extensions

import com.google.android.material.textfield.TextInputLayout
import java.text.DateFormat

import java.text.DateFormat.getDateInstance
import java.util.*

fun Date.format(): String{
    return getDateInstance(DateFormat.SHORT).format(this)
}

var TextInputLayout.text : String
    get() = editText?.text?.toString() ?: ""
    set(value){
        editText?.setText(value)
    }

