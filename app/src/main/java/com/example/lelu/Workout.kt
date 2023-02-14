package com.example.lelu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.example.lelu.databinding.ActivityWorkoutBinding

//toast: Toast.makeText(applicationContext,"message", Toast.LENGTH_LONG).show()

class Workout : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutBinding

    private var pushUpsCounter = 0
    private var pullUpsCounter = 0
    private var dipsCounter = 0

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
                }
                binding.editTextIncDecPushUps.text.clear()
                binding.editTextIncDecPushUps.clearFocus()
            }
            resources.getString(R.string.PullUps) -> {
                if(binding.editTextIncDecPullUps.text.toString() == ""){
                    binding.textPullUps.text = "0"
                    binding.textPullUps.clearFocus()
                }
                binding.editTextIncDecPullUps.text.clear()
                binding.editTextIncDecPullUps.clearFocus()
            }
            resources.getString(R.string.Dips) -> {
                if(binding.editTextIncDecDips.text.toString() == ""){
                    binding.textDips.text = "0"
                    binding.textDips.clearFocus()
                }
                binding.editTextIncDecDips.text.clear()
                binding.editTextIncDecDips.clearFocus()
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
                    pushUpsCounter += Integer.parseInt(binding.editTextIncDecPushUps.text.toString())
                }else{
                    pushUpsCounter++
                }
                binding.textPushUps.text = pushUpsCounter.toString()
            }
            resources.getString(R.string.PullUps) -> {
                if(binding.editTextIncDecPullUps.text.toString() != ""){
                    pullUpsCounter += Integer.parseInt(binding.editTextIncDecPullUps.text.toString())
                }else{
                    pullUpsCounter++
                }
                binding.textPullUps.text = pullUpsCounter.toString()
            }
            resources.getString(R.string.Dips) -> {
                if(binding.editTextIncDecDips.text.toString() != ""){
                    dipsCounter += Integer.parseInt(binding.editTextIncDecDips.text.toString())
                }else{
                    dipsCounter++
                }
                binding.textDips.text = dipsCounter.toString()
            }
            else -> { }
        }
    }

    fun onClickSubtract(view: View){
        val tag:String = view.tag.toString()
        onClickSubtract(tag)
    }
    private fun onClickSubtract(tag: String){
        when(tag){
            resources.getString(R.string.PushUps) -> {
                if(binding.editTextIncDecPushUps.text.toString() != ""){
                    pushUpsCounter -= Integer.parseInt(binding.editTextIncDecPushUps.text.toString())
                }else{
                    pushUpsCounter--
                }
                binding.textPushUps.text = pushUpsCounter.toString()
            }
            resources.getString(R.string.PullUps) -> {
                if(binding.editTextIncDecPullUps.text.toString() != ""){
                    pullUpsCounter -= Integer.parseInt(binding.editTextIncDecPullUps.text.toString())
                }else{
                    pullUpsCounter--
                }
                binding.textPullUps.text = pullUpsCounter.toString()
            }
            resources.getString(R.string.Dips) -> {
                if(binding.editTextIncDecDips.text.toString() != ""){
                    dipsCounter -= Integer.parseInt(binding.editTextIncDecDips.text.toString())
                }else{
                    dipsCounter--
                }
                binding.textDips.text = dipsCounter.toString()
            }
            else -> { }
        }
    }

    fun onClickGoBack(view: View){
        finish()
    }
}