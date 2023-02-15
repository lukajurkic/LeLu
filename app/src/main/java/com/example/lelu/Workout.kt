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

//toast: Toast.makeText(applicationContext,"message", Toast.LENGTH_LONG).show()

class Workout : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutBinding

    private var pushUpsCounter = 0
    private var pullUpsCounter = 0
    private var dipsCounter = 0

    var timer = Timer()
    var timerTask: TimerTask? = null
    private var time = 0.0f
    private var running = false

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

    }

    //reset's individual workout layout
    fun onClickClear(view: View){
        val tag:String = view.tag.toString()
        onClickClear(tag)
    }
    private fun onClickClear(tag : String){
        when(tag){
            resources.getString(R.string.PushUps) -> {
                if (binding.editTextIncDecPushUps.text.toString() == ""){
                    binding.textPushUps.text = "0"
                    binding.textPushUps.clearFocus()
                    pushUpsCounter = 0
                }
                binding.editTextIncDecPushUps.text.clear()
                binding.editTextIncDecPushUps.clearFocus()
            }
            resources.getString(R.string.PullUps) -> {
                if(binding.editTextIncDecPullUps.text.toString() == ""){
                    binding.textPullUps.text = "0"
                    binding.textPullUps.clearFocus()
                    pullUpsCounter = 0
                }
                binding.editTextIncDecPullUps.text.clear()
                binding.editTextIncDecPullUps.clearFocus()
            }
            resources.getString(R.string.Dips) -> {
                if(binding.editTextIncDecDips.text.toString() == ""){
                    binding.textDips.text = "0"
                    binding.textDips.clearFocus()
                    dipsCounter = 0
                }
                binding.editTextIncDecDips.text.clear()
                binding.editTextIncDecDips.clearFocus()
            }
            resources.getString(R.string.Delete) -> {
                val builder = AlertDialog.Builder(this@Workout)
                builder.setMessage("Are you sure you want to Delete")
                    .setCancelable(false)
                        //https://stackoverflow.com/questions/33437398/how-to-change-textcolor-in-alertdialog  -->  changing color, font, background in alterDialog box
                    .setPositiveButton(Html.fromHtml("<font color='#2680FF'>Yes</font>")) { dialog, id ->
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
                        pushUpsCounter = 0
                        pullUpsCounter = 0
                        dipsCounter = 0
                    }
                    .setNegativeButton(Html.fromHtml("<font color='#2680FF'>No</font>")) { dialog, id -> dialog.dismiss()}
                val alter = builder.create()
                alter.show()
            }
            else -> { }
        }
    }

    //adds repetitions
    fun onClickAdd(view: View){
        val tag:String = view.tag.toString()
        onClickAdd(tag)
    }
    private fun onClickAdd(tag: String){
        //when == switch case
        when(tag){
            resources.getString(R.string.PushUps) -> {
                if(binding.editTextIncDecPushUps.text.toString() != ""){
                    if(pushUpsCounter <= 9999)pushUpsCounter += Integer.parseInt(binding.editTextIncDecPushUps.text.toString())
                }else{
                    if(pushUpsCounter <= 9999)pushUpsCounter++
                }
                binding.textPushUps.text = pushUpsCounter.toString()
            }
            resources.getString(R.string.PullUps) -> {
                if(binding.editTextIncDecPullUps.text.toString() != ""){
                    if(pullUpsCounter <= 9999)pullUpsCounter += Integer.parseInt(binding.editTextIncDecPullUps.text.toString())
                }else{
                    if(pullUpsCounter <= 9999)pullUpsCounter++
                }
                binding.textPullUps.text = pullUpsCounter.toString()
            }
            resources.getString(R.string.Dips) -> {
                if(binding.editTextIncDecDips.text.toString() != ""){
                    if(dipsCounter <= 9999)dipsCounter += Integer.parseInt(binding.editTextIncDecDips.text.toString())
                }else{
                    if(dipsCounter <= 9999)dipsCounter++
                }
                binding.textDips.text = dipsCounter.toString()
            }
            else -> { }
        }
    }

    //subtracts repetitions
    fun onClickSubtract(view: View){
        val tag:String = view.tag.toString()
        onClickSubtract(tag)
    }
    private fun onClickSubtract(tag: String){
        when(tag){
            resources.getString(R.string.PushUps) -> {
                if(binding.editTextIncDecPushUps.text.toString() != ""){
                    val toSubtract = Integer.parseInt(binding.editTextIncDecPushUps.text.toString())
                    if(pushUpsCounter - toSubtract >= 0)pushUpsCounter -= toSubtract
                    else pushUpsCounter = 0;
                }else{
                    if(pushUpsCounter >= 1)pushUpsCounter--
                }
                binding.textPushUps.text = pushUpsCounter.toString()
            }
            resources.getString(R.string.PullUps) -> {
                if(binding.editTextIncDecPullUps.text.toString() != ""){
                    val toSubtract = Integer.parseInt(binding.editTextIncDecPullUps.text.toString())
                    if(pullUpsCounter - toSubtract >= 0)pullUpsCounter -= toSubtract
                    else pullUpsCounter = 0;
                }else{
                    if(pullUpsCounter >= 1)pullUpsCounter--
                }
                binding.textPullUps.text = pullUpsCounter.toString()
            }
            resources.getString(R.string.Dips) -> {
                if(binding.editTextIncDecDips.text.toString() != ""){
                    val toSubtract = Integer.parseInt(binding.editTextIncDecDips.text.toString())
                    if(dipsCounter - toSubtract >= 0)dipsCounter -= toSubtract
                    else dipsCounter = 0;
                }else{
                    if(dipsCounter >= 1)dipsCounter--
                }
                binding.textDips.text = dipsCounter.toString()
            }
            else -> { }
        }
    }

    //timer methods
    private fun startTimer(){
        timerTask = object : TimerTask() {
            override fun run() {
                (this@Workout as Activity).runOnUiThread {
                    time++
                    binding.cChronometer.setText(getTimerText())
                }
            }
        }
        timer.scheduleAtFixedRate(timerTask, 0, 1000)
    }
    private fun getTimerText(): String? {
        val rounded = Math.round(time)
        val seconds = rounded % 86400 % 3600 % 60
        val minutes = rounded % 86400 % 3600 / 60
        val hours = rounded % 86400 / 3600
        return formatTime(seconds, minutes, hours)
    }
    private fun formatTime(seconds: Int, minutes: Int, hours: Int): String? {
        return String.format("%02d", hours) + ":" + String.format(
            "%02d", minutes) + ":" + String.format("%02d", seconds)
    }
    fun pauseChronometer(view: View){
        if(running){
            timerTask?.cancel()
            running = false
            binding.buttonPlayTimer.visibility  = View.VISIBLE
            binding.buttonPauseTimer.visibility = View.GONE
        }
    }
    fun resetChronometer(view: View){
        if(timerTask != null){
            timerTask!!.cancel()
            running = false
            time = 0.0F
            binding.cChronometer.text = formatTime(0,0,0)
            binding.buttonPlayTimer.visibility  = View.VISIBLE
            binding.buttonPauseTimer.visibility = View.GONE
        }
    }
    fun startChronometer(view: View){
        if(!running){
            startTimer()
            running = true
            binding.buttonPlayTimer.visibility  = View.GONE
            binding.buttonPauseTimer.visibility = View.VISIBLE
        }
    }

    //goes back to main manu
    fun onClickGoBack(view: View){
        finish()
    }
}