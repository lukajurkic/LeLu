package com.example.lelu

import android.app.Activity
import android.content.Context
import android.view.View
import com.example.lelu.databinding.ActivityWorkoutBinding
import java.util.*
import kotlin.math.roundToInt

class TimeHandler(val context: Context, val binding: ActivityWorkoutBinding){
    private var timer = Timer()
    private var timerTask: TimerTask? = null
    private var time = 0.0f
    private var running = false
    fun onClickChronometer(view: View){
        when(view.tag.toString()){
            context.resources.getString(R.string.Pause) -> pauseChronometer()
            context.resources.getString(R.string.Play) -> startChronometer()
            context.resources.getString(R.string.Reset) -> resetChronometer()
        }
    }
    private fun startTimer(){
        timerTask = object : TimerTask() {
            override fun run() {
                (context as Activity).runOnUiThread {
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
}