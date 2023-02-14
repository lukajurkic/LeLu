package com.example.lelu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.lelu.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        val view = binding.root
        setContentView(view)

        //remove action bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()

        binding.root.setOnClickListener{
            val intent = Intent(this@MainActivity, MainMenu::class.java)
            startActivity(intent)
        }


        val animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.imageViewLeLuTools.startAnimation(animationFadeIn)
        animationFadeIn.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) { }
            override fun onAnimationRepeat(p0: Animation?) { }
            override fun onAnimationEnd(animation: Animation?) {
                val intent = Intent(this@MainActivity, MainMenu::class.java)
                startActivity(intent)
            }
        })
    }
}