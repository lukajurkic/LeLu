package com.example.lelu.WorkoutHandler

import com.example.lelu.WorkoutHandler.Exercise

class PushUp(type: String) : Exercise(type) {
    override fun toString(): String {
        return "Push_ups " + super.toString()
    }
}