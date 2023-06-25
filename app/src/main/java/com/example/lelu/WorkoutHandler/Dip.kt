package com.example.lelu.WorkoutHandler

class Dip(type: String) : Exercise(type) {
    override fun toString(): String {
        return "Dips " + super.toString()
    }
}