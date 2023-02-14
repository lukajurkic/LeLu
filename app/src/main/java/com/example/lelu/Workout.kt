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
    fun onClickClear(view: View){
        val something:String = view.tag.toString()
        onClickClear(something)
    }
    private fun onClickClear(something : String){
        if (something == resources.getString(R.string.PushUps)){
            binding.textPushUps.text = "0"
            binding.textPushUps.clearFocus()
            binding.editTextIncDecPushUps.text.clear()
            binding.editTextIncDecPushUps.clearFocus()
        }
        else if(something == resources.getString(R.string.PullUps)){
            binding.textPullUps.text = "0"
            binding.textPullUps.clearFocus()
            binding.editTextIncDecPullUps.text.clear()
            binding.editTextIncDecPullUps.clearFocus()
        }
        else if(something == resources.getString(R.string.Dips)){
            binding.textDips.text = "0"
            binding.textDips.clearFocus()
            binding.editTextIncDecDips.text.clear()
            binding.editTextIncDecDips.clearFocus()
        }
    }

    fun onClickGoBack(view: View){
        finish()
    }
}