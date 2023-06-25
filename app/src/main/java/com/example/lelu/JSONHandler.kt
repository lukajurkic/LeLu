package com.example.lelu

import com.example.lelu.WorkoutHandler.Training
import com.google.gson.Gson

class JSONHandler{
    fun getJSON(training: Training): String {
        val gson = Gson()
        return gson.toJson(training)
    }
}