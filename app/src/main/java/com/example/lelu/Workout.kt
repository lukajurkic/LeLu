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

class Workout : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutBinding

    //counters (dictionary)
    private val counters = mutableMapOf<String, Int>()

    private var timer = Timer()
    private var timerTask: TimerTask? = null
    private var time = 0.0f
    private var running = false

    private var saved = false
    private var id = -1 //id of workout, -1 if not saved, other number if saved
    private val maxReps = 9999

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

        //initialising counters
        resetCounters()

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
                    counters[resources.getString(R.string.PushUps)] = 0
                }
                binding.editTextIncDecPushUps.text.clear()
                binding.editTextIncDecPushUps.clearFocus()
            }
            resources.getString(R.string.PullUps) -> {
                if(binding.editTextIncDecPullUps.text.toString() == ""){
                    binding.textPullUps.text = "0"
                    binding.textPullUps.clearFocus()
                    counters[resources.getString(R.string.PullUps)] = 0
                }
                binding.editTextIncDecPullUps.text.clear()
                binding.editTextIncDecPullUps.clearFocus()
            }
            resources.getString(R.string.Dips) -> {
                if(binding.editTextIncDecDips.text.toString() == ""){
                    binding.textDips.text = "0"
                    binding.textDips.clearFocus()
                    counters[resources.getString(R.string.Dips)] = 0
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
                    .setPositiveButton(Html.fromHtml("<font color='#2680FF'>Yes</font>")) { _, _ -> resetCounters() }
                    .setNegativeButton(Html.fromHtml("<font color='#2680FF'>No</font>")) { _, _ ->
                        onClickSave(view)
                        resetCounters()
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
                if(binding.editTextIncDecPushUps.text.toString() != ""){
                    if(counters[resources.getString(R.string.PushUps)]!! <= maxReps)
                    //dictionaryName[Key] += newValue
                        counters.inc(resources.getString(R.string.PushUps), Integer.parseInt(binding.editTextIncDecPushUps.text.toString()))
                }else{
                    if(counters[resources.getString(R.string.PushUps)]!! <= maxReps)
                    //dictionaryName[Key]++
                        counters.inc(resources.getString(R.string.PushUps), 1)
                }
                binding.textPushUps.text = counters[resources.getString(R.string.PushUps)].toString()
            }
            resources.getString(R.string.PullUps) -> {
                if(binding.editTextIncDecPullUps.text.toString() != ""){
                    if(counters[resources.getString(R.string.PullUps)]!! <= maxReps)
                        counters.inc(resources.getString(R.string.PullUps), Integer.parseInt(binding.editTextIncDecPullUps.text.toString()))
                }else{
                    if(counters[resources.getString(R.string.PullUps)]!! <= maxReps)
                        counters.inc(resources.getString(R.string.PullUps), 1)
                }
                binding.textPullUps.text = counters[resources.getString(R.string.PullUps)].toString()
            }
            resources.getString(R.string.Dips) -> {
                if(binding.editTextIncDecDips.text.toString() != ""){
                    if(counters[resources.getString(R.string.Dips)]!! <= maxReps)
                        counters.inc(resources.getString(R.string.Dips), Integer.parseInt(binding.editTextIncDecDips.text.toString()))
                }else{
                    if(counters[resources.getString(R.string.Dips)]!! <= maxReps)
                        counters.inc(resources.getString(R.string.Dips), 1)
                }
                binding.textDips.text = counters[resources.getString(R.string.Dips)].toString()
            }
            else -> { }
        }
    }

    //subtracts repetitions
    fun onClickSubtract(view: View){
        saved = false
        when(view.tag.toString()){
            resources.getString(R.string.PushUps) -> {
                if(binding.editTextIncDecPushUps.text.toString() != ""){
                    val toSubtract = Integer.parseInt(binding.editTextIncDecPushUps.text.toString())
                    if(counters[resources.getString(R.string.PushUps)]!! - toSubtract >= 0)
                        counters.inc(resources.getString(R.string.PushUps), -toSubtract)
                    else counters[resources.getString(R.string.PushUps)] = 0
                }else{
                    if(counters[resources.getString(R.string.PushUps)]!! >= 1)
                        counters.inc(resources.getString(R.string.PushUps), -1)
                }
                binding.textPushUps.text = counters[resources.getString(R.string.PushUps)].toString()
            }
            resources.getString(R.string.PullUps) -> {
                if(binding.editTextIncDecPullUps.text.toString() != ""){
                    val toSubtract = Integer.parseInt(binding.editTextIncDecPullUps.text.toString())
                    if(counters[resources.getString(R.string.PullUps)]!! - toSubtract >= 0)
                        counters.inc(resources.getString(R.string.PullUps), -toSubtract)
                    else counters[resources.getString(R.string.PullUps)] = 0
                }else{
                    if(counters[resources.getString(R.string.PullUps)]!! >= 1)
                        counters.inc(resources.getString(R.string.PullUps), -1)
                }
                binding.textPullUps.text = counters[resources.getString(R.string.PullUps)].toString()
            }
            resources.getString(R.string.Dips) -> {
                if(binding.editTextIncDecDips.text.toString() != ""){
                    val toSubtract = Integer.parseInt(binding.editTextIncDecDips.text.toString())
                    if(counters[resources.getString(R.string.Dips)]!! - toSubtract >= 0)
                        counters.inc(resources.getString(R.string.Dips), -toSubtract)
                    else counters[resources.getString(R.string.Dips)] = 0
                }else{
                    if(counters[resources.getString(R.string.Dips)]!! >= 1)
                        counters.inc(resources.getString(R.string.Dips), -1)
                }
                binding.textDips.text = counters[resources.getString(R.string.Dips)].toString()
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
                .setNegativeButton(Html.fromHtml("<font color='#2680FF'>No</font>")) { _, _ -> finish() }
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

    private fun resetCounters(){
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
        counters[resources.getString(R.string.PushUps)] = 0
        counters[resources.getString(R.string.PullUps)] = 0
        counters[resources.getString(R.string.Dips)] = 0
    }

    //my method for increasing value in dictionary, source:
    //https://stackoverflow.com/questions/53826903/increase-value-in-mutable-map
    fun <T> MutableMap<T, Int>.inc(key: T, more: Int = 1) = merge(key, more, Int::plus)
    fun <T> MutableMap<T, Int>.dec(key: T, more: Int = 1) = merge(key, more, Int::minus)

}

