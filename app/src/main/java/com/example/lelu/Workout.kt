package com.example.lelu

import android.app.Activity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.lelu.databinding.ActivityWorkoutBinding
import java.util.*
import kotlin.math.roundToInt


//toast: Toast.makeText(applicationContext,"message", Toast.LENGTH_LONG).show()

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
        return "$Type : $Counter"
    }
}
class PushUp(type: String) : Exercise(type) { }
class PullUp(type: String) : Exercise(type) { }
class Dip(type: String) : Exercise(type) { }
open class Training private constructor(){

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
        TODO("Not yet implemented")
    }
}

class Workout : AppCompatActivity() {

    private lateinit var training: Training
    private lateinit var pullUp: PullUp
    private lateinit var pushUp: PushUp
    private lateinit var dip: Dip

    private lateinit var binding: ActivityWorkoutBinding

    private var timer = Timer()
    private var timerTask: TimerTask? = null
    private var time = 0.0f
    private var running = false

    private var saved = false
    private var id = -1 //id of workout, -1 if not saved, other number if saved

    //checking for each layer if expanded
    private val expanded = mutableMapOf<String,Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //remove action bar - old version
        supportActionBar!!.hide()
        //change color of status bar
        val window = this@Workout.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        //adding values to dictionary: name.put(K,V) or name[K] = V
        expanded[resources.getString(R.string.PushUps)] = false
        expanded[resources.getString(R.string.PullUps)] = false
        expanded[resources.getString(R.string.Dips)] = false

        loadTraining()



    }
    private fun loadTraining(){
        training = Training.getInstance()
            loadNewTraining()
    }
    private fun loadOldTraining(): Boolean{
        //check if unsaved workout exist, in JSON file look at the first row
        var returnValue = false
        val builder = AlertDialog.Builder(this@Workout)
        builder
            .setTitle("Restore workout")
            .setMessage("You have unsaved workout, do you want to restore it?")
            //https://stackoverflow.com/questions/33437398/how-to-change-textcolor-in-alertdialog  -->  changing color, font, background in alterDialog box
            .setPositiveButton(Html.fromHtml("<font color='#2680FF'>Yes</font>")) { _, _ ->
                //restore old TODO()
                returnValue = true
            }
            .setNegativeButton(Html.fromHtml("<font color='#2680FF'>No</font>")) { _, _ ->
                returnValue = false
                reset()
            }
        val alter = builder.create()
        alter.show()
        return returnValue
    }
    private fun loadNewTraining(){
        pullUp = PullUp("Regular pull up")
        pushUp = PushUp("Regular push up")
        dip = Dip("Regular dip")

        training.AddExercises(pullUp)
        training.AddExercises(pushUp)
        training.AddExercises(dip)
    }

    //expand methods -- currently unused
     fun onClickExpand(view: View){
        when(val tag: String = view.tag.toString()){
            resources.getString(R.string.PushUps) -> {
                if (expanded[tag] == true) {
                    binding.expandLayoutPushUps.visibility = View.GONE
                    expanded[tag] = false
                    binding.buttonExpandPushUps.rotation = 0F
                }
                else {
                    binding.expandLayoutPushUps.visibility = View.VISIBLE
                    expanded[tag] = true
                    binding.buttonExpandPushUps.rotation = 180F
                }
            }
            resources.getString(R.string.PullUps) -> {
                if (expanded[tag] == true) {
                    binding.expandLayoutPullUps.visibility = View.GONE
                    expanded[tag] = false
                    binding.buttonExpandPullUps.rotation = 0F
                }
                else {
                    binding.expandLayoutPullUps.visibility = View.VISIBLE
                    expanded[tag] = true
                    binding.buttonExpandPullUps.rotation = 180F
                }
            }
            resources.getString(R.string.Dips) -> {
                if (expanded[tag] == true) {
                    binding.expandLayoutDips.visibility = View.GONE
                    expanded[tag] = false
                    binding.buttonExpandDips.rotation = 0F
                }
                else {
                    binding.expandLayoutDips.visibility = View.VISIBLE
                    expanded[tag] = true
                    binding.buttonExpandDips.rotation = 180F
                }
            }
            else -> {}
        }
     }

    //safe resets workout
    fun onClickClear(view: View) {
        when(view.tag.toString()){
            resources.getString(R.string.PushUps) -> {
                if (binding.editTextIncDecPushUps.text.toString() == ""){
                    binding.textPushUps.text = "0"
                    binding.textPushUps.clearFocus()
                    training.Reset(pushUp)
                }
                binding.editTextIncDecPushUps.text.clear()
                binding.editTextIncDecPushUps.clearFocus()
            }
            resources.getString(R.string.PullUps) -> {
                if(binding.editTextIncDecPullUps.text.toString() == ""){
                    binding.textPullUps.text = "0"
                    binding.textPullUps.clearFocus()
                    training.Reset(pullUp)
                }
                binding.editTextIncDecPullUps.text.clear()
                binding.editTextIncDecPullUps.clearFocus()
            }
            resources.getString(R.string.Dips) -> {
                if(binding.editTextIncDecDips.text.toString() == ""){
                    binding.textDips.text = "0"
                    binding.textDips.clearFocus()
                    training.Reset(dip)
                }
                binding.editTextIncDecDips.text.clear()
                binding.editTextIncDecDips.clearFocus()
            }
            resources.getString(R.string.Delete) -> {
                val builder = AlertDialog.Builder(this@Workout)
                builder
                    .setTitle("Delete workout")
                    .setMessage("Are you sure you want to delete without saving?")
                    .setNeutralButton(Html.fromHtml("<font color='#2680FF'>Cancel</font>")) { dialog, _ -> dialog.dismiss()}
                    //https://stackoverflow.com/questions/33437398/how-to-change-textcolor-in-alertdialog  -->  changing color, font, background in alterDialog box
                    .setPositiveButton(Html.fromHtml("<font color='#2680FF'>Yes</font>")) { _, _ -> reset() }
                    .setNegativeButton(Html.fromHtml("<font color='#2680FF'>No</font>")) { _, _ ->
                        onClickSave(view)
                        reset()
                    }
                val alter = builder.create()
                alter.show()
            }
            else -> { }
        }
    }

    //adds repetitions
    fun onClickAdd(view: View){
        saved = false
        //when == switch case
        when(view.tag.toString()){
            resources.getString(R.string.PushUps) -> {
                (if(binding.editTextIncDecPushUps.text?.toString() == "") 1 else binding.editTextIncDecPushUps.text?.toString()
                    ?.let { Integer.parseInt(it) })
                        ?.let { training.AddRepetitions(pushUp, it ) }
                binding.textPushUps.text = training.GetRepetitions(pushUp).toString()
            }
            resources.getString(R.string.PullUps) -> {
                (if(binding.editTextIncDecPullUps.text?.toString() == "") 1 else binding.editTextIncDecPullUps.text?.toString()
                    ?.let { Integer.parseInt(it) })
                        ?.let { training.AddRepetitions(pullUp, it ) }
                binding.textPullUps.text = training.GetRepetitions(pullUp).toString()
            }
            resources.getString(R.string.Dips) -> {
                (if(binding.editTextIncDecDips.text?.toString() == "") 1 else binding.editTextIncDecDips.text?.toString()
                    ?.let { Integer.parseInt(it) })
                        ?.let { training.AddRepetitions(dip, it ) }
                binding.textDips.text = training.GetRepetitions(dip).toString()
            }
            else -> { }
        }
    }

    //subtracts repetitions
    fun onClickSubtract(view: View){
        saved = false
        when(view.tag.toString()){
            resources.getString(R.string.PushUps) -> {
                (if(binding.editTextIncDecPushUps.text?.toString() == "") 1 else binding.editTextIncDecPushUps.text?.toString()
                    ?.let { Integer.parseInt(it) })
                    ?.let { training.SubstractRepetitions(pushUp, it ) }
                binding.textPushUps.text = training.GetRepetitions(pushUp).toString()
            }
            resources.getString(R.string.PullUps) -> {
                (if(binding.editTextIncDecPullUps.text?.toString() == "") 1 else binding.editTextIncDecPullUps.text?.toString()
                    ?.let { Integer.parseInt(it) })
                    ?.let { training.SubstractRepetitions(pullUp, it ) }
                binding.textPullUps.text = training.GetRepetitions(pullUp).toString()
            }
            resources.getString(R.string.Dips) -> {
                (if(binding.editTextIncDecDips.text?.toString() == "") 1 else binding.editTextIncDecDips.text?.toString()
                    ?.let { Integer.parseInt(it) })
                    ?.let { training.SubstractRepetitions(dip, it ) }
                binding.textDips.text = training.GetRepetitions(dip).toString()
            }
            else -> { }
        }
    }

    //timer methods
    fun onClickChronometer(view: View){
        when(view.tag.toString()){
            resources.getString(R.string.Pause) -> pauseChronometer()
            resources.getString(R.string.Play) -> startChronometer()
            resources.getString(R.string.Reset) -> resetChronometer()
        }
    }
    private fun startTimer(){
        timerTask = object : TimerTask() {
            override fun run() {
                (this@Workout as Activity).runOnUiThread {
                    time++
                    binding.cChronometer.text = getTimerText()
                }
            }
        }
        timer.scheduleAtFixedRate(timerTask, 0, 1000)
    }
    private fun getTimerText(): String {
        val rounded = time.roundToInt()
        val seconds = rounded % 86400 % 3600 % 60
        val minutes = rounded % 86400 % 3600 / 60
        val hours = rounded % 86400 / 3600
        return formatTime(seconds, minutes, hours)
    }
    private fun formatTime(seconds: Int, minutes: Int, hours: Int): String {
        return String.format("%02d", hours) + ":" + String.format(
            "%02d", minutes) + ":" + String.format("%02d", seconds)
    }
    private fun pauseChronometer(){
        if(running){
            timerTask?.cancel()
            running = false
            binding.buttonPlayTimer.visibility  = View.VISIBLE
            binding.buttonPauseTimer.visibility = View.GONE
        }
    }
    private fun resetChronometer(){
        if(timerTask != null){
            timerTask!!.cancel()
            running = false
            time = 0.0F
            binding.cChronometer.text = formatTime(0,0,0)
            binding.buttonPlayTimer.visibility  = View.VISIBLE
            binding.buttonPauseTimer.visibility = View.GONE
        }
    }
    private fun startChronometer(){
        if(!running){
            startTimer()
            running = true
            binding.buttonPlayTimer.visibility  = View.GONE
            binding.buttonPauseTimer.visibility = View.VISIBLE
        }
    }

    //goes back to main manu
    fun onClickGoBack(view: View){
        if(saved){
            finish()
        }
        else {
            val builder = AlertDialog.Builder(this@Workout)
            builder.setMessage("Do you want to save changes to 'workoutName'")
                .setTitle("Workout tool")
                .setNeutralButton(Html.fromHtml("<font color='#2680FF'>Cancel</font>")) { dialog, _ -> dialog.dismiss()}
                //https://stackoverflow.com/questions/33437398/how-to-change-textcolor-in-alertdialog  -->  changing color, font, background in alterDialog box
                .setNegativeButton(Html.fromHtml("<font color='#2680FF'>No</font>")) { _, _ ->
                    finish()
                }
                .setPositiveButton(Html.fromHtml("<font color='#2680FF'>Yes</font>")) { _, _ -> onClickSave(view) }
            val alter = builder.create()
            alter.show()
        }
    }


    fun onClickSave(view: View) {
        if(!saved){
            if(id == -1) {
                //save workout
            }
            else{
                //update workout
            }
        }
        saved = true
    }

    fun onClickSeeHistory(view: View){
        //implement shoving history of workouts, from database
    }

    private fun reset(){
        //clear push ups
        binding.textPushUps.text = "0"
        binding.textPushUps.clearFocus()
        binding.editTextIncDecPushUps.text.clear()
        binding.editTextIncDecPushUps.clearFocus()
        //clear pull ups
        binding.textPullUps.text = "0"
        binding.textPullUps.clearFocus()
        binding.editTextIncDecPullUps.text.clear()
        binding.editTextIncDecPullUps.clearFocus()
        //clear dips
        binding.textDips.text = "0"
        binding.textDips.clearFocus()
        binding.editTextIncDecDips.text.clear()
        binding.editTextIncDecDips.clearFocus()
        //set counters to zero
        training.Reset()
    }

    //my method for increasing value in dictionary, source:
    //https://stackoverflow.com/questions/53826903/increase-value-in-mutable-map
    fun <T> MutableMap<T, Int>.inc(key: T, more: Int = 1) = merge(key, more, Int::plus)
    fun <T> MutableMap<T, Int>.dec(key: T, more: Int = 1) = merge(key, more, Int::minus)

}

