package com.example.lelu.WorkoutHandler

import com.example.lelu.WorkoutHandler.Exercise

class PullUp(type: String) : Exercise(type) {
    override fun toString(): String {
        return "Pull_ups " + super.toString()
    }
}