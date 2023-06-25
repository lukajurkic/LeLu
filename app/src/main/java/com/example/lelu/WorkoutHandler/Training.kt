package com.example.lelu.WorkoutHandler

class Training private constructor(){

    //companion object = static
    companion object{
        private var instance: Training? = null
        fun getInstance()=
            instance ?: synchronized(this) {
                instance ?: Training().also { instance = it }
            }
    }

    private val exercises = mutableListOf<Exercise>()

    fun AddExercises(exercise: Exercise){
        this.exercises.add(exercise)
    }
    fun RemoveExercise(exercise: Exercise){
        this.exercises.remove(exercise)
    }
    fun AddRepetitions(exercise: Exercise, count: Int){
        val index: Int = this.exercises.indexOf(exercise)
        this.exercises[index].Counter += count
    }
    fun SubstractRepetitions(exercise: Exercise, count: Int){
        val index: Int = this.exercises.indexOf(exercise)
        this.exercises[index].Counter -= count
    }

    fun GetWorkout(): List<Exercise> {
        return exercises
    }
    fun GetRepetitions(exercise: Exercise): Int{
        val index: Int = this.exercises.indexOf(exercise)
        return this.exercises[index].Counter
    }

    fun Reset(){
        for (excercise in exercises){
            excercise.Reset()
        }
    }
    fun Reset(exercise: Exercise){
        val index: Int = this.exercises.indexOf(exercise)
        this.exercises[index].Reset()
    }

    override fun toString(): String {
        var result: String = "Training ["
        for (exercise in exercises){
            result.plus(exercises)
        }
        result.plus(" ]")
        return result
    }
}