package com.example.lelu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import com.example.lelu.databinding.ActivityMainMenuBinding
import kotlin.system.exitProcess

class MainMenu : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(binding.textViewLeLu,70,100,5,
            TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM)
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(binding.textViewTools,70,100,5,
            TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM)

        //remove action bar - old version
        supportActionBar!!.hide()
        //change color of status bar
        val window = this@MainMenu.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
    }
    //disables going back to activityMain
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() { }

    fun onClickChangeActivity(view: View){
        val tag: String = view.tag.toString()
        onClickChangeActivity(tag)
    }
    private fun onClickChangeActivity(tag: String){
        val animationBounce = AnimationUtils.loadAnimation(this, R.anim.bounce)
        var intent = Intent(this@MainMenu, MainMenu::class.java)
        when(tag){
            resources.getString(R.string.FuelTool) -> {
                binding.btnFuel.startAnimation(animationBounce)
                intent = Intent(this@MainMenu, Fuel::class.java)
            }
            resources.getString(R.string.WorkoutTool) -> {
                binding.btnWorkout.startAnimation(animationBounce)
                intent = Intent(this@MainMenu, Workout::class.java)
            }
            resources.getString(R.string.MatrixCalculator) -> {
                binding.btnMatrix.startAnimation(animationBounce)
                intent = Intent(this@MainMenu, Matrix::class.java)
            }
            resources.getString(R.string.Exit) -> {
                finishAffinity()
                exitProcess(0)
            }
            else -> { }
        }
        animationBounce.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) { }
            override fun onAnimationRepeat(p0: Animation?) { }
            override fun onAnimationEnd(animation: Animation?) {
                startActivity(intent)
            }
        })
    }
}