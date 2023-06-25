package com.example.lelu.WorkoutHandler

abstract class Exercise(var exerciseType: String) {
    var Counter: Int = 0
        set(value) {
            field = if(value in 0..9999)
                value
            else 0
        }
    var Type: String = exerciseType
    fun Reset(){
        Counter = 0
    }
    override fun toString(): String {
        return "{$Type: $Counter}"
    }
}