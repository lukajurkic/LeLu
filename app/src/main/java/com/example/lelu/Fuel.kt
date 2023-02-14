package com.example.lelu

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.lelu.Fuel.myConstants.convertMpgUK
import com.example.lelu.Fuel.myConstants.convertMpgUSA
import com.example.lelu.databinding.ActivityFuelBinding


class Fuel : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityFuelBinding

    object myConstants{
        private const val mileToKm = 1.609344f
        private const val UKGallonToLiter = 4.546092f
        private const val USAGallonToLiter = 3.7854118f
        const val convertMpgUK = mileToKm / UKGallonToLiter // result - km/liter
        const val convertMpgUSA = mileToKm / USAGallonToLiter  // result - km/liter
    }
    // To get liter per 100 km - 100/(mpgUK * convertMpgUK)
    // or 100/(mpgUSA * convertMpgUSA)

    private var fedttxtFuelPrice = 0f
    private var fedttxtFuelConsumption = 0f
    private var fedttxtDistance = 0f

    enum class FuelConsumptionType {
        lp100km, mpgUK, mpgUSA
    }

    private var fuelConsumptionType = FuelConsumptionType.lp100km

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFuelBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //remove action bar - old version
        supportActionBar!!.hide()
        //change color of status bar
        val window = this@Fuel.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.spnFuelConsumption,
            android.R.layout.simple_spinner_dropdown_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnFuelConsumption.adapter = adapter
        binding.spnFuelConsumption.onItemSelectedListener = this


    }
    fun btnOnClkCalculate(v: View?) {
        if (!(binding.edttxtFuelPrice.text.toString()
                .trim { it <= ' ' }.isEmpty() || binding.edttxtFuelConsumption.text.toString()
                .trim { it <= ' ' }.isEmpty() || binding.edttxtDistance.text.toString()
                .trim { it <= ' ' }.isEmpty())
        ) {
            fedttxtFuelPrice = binding.edttxtFuelPrice.text.toString().toFloat()
            fedttxtFuelConsumption = binding.edttxtFuelConsumption.text.toString().toFloat()
            fedttxtDistance = binding.edttxtDistance.text.toString().toFloat()
            val result: Float = when (fuelConsumptionType) {
                FuelConsumptionType.lp100km -> {
                    fedttxtDistance / 100 * fedttxtFuelConsumption * fedttxtFuelPrice
                }
                FuelConsumptionType.mpgUK -> {
                    fedttxtDistance / 100 * 100 / (fedttxtFuelConsumption * convertMpgUK) * fedttxtFuelPrice
                }
                else -> /* if(fuelConsumptionType == FuelConsumptionType.mpgUSA*/ {
                    fedttxtDistance / 100 * 100 / (fedttxtFuelConsumption * convertMpgUSA) * fedttxtFuelPrice
                }
            }
            binding.txtPriceValue.text = result.toString()
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //val text = adapterView.getItemAtPosition(i).toString()
        fuelConsumptionType =
            if (position == 0) FuelConsumptionType.lp100km else if (position == 1) FuelConsumptionType.mpgUK else FuelConsumptionType.mpgUSA // if(text == getResources().getStringArray(R.array.spnFuelConsumption)[2])
        if (parent != null) {
            Log.d("adapter child count", parent.count.toString())
        }
        if (parent != null) {
            if (parent.getChildAt(0) != null) {
                Log.d("adapter child", "adapter child is not null")
                // Causes program to crash when changing dark mode setting
                //---------------------------------------------------------------
                (parent.getChildAt(0) as TextView).setTextColor(Color.parseColor("#FFFFFF"))
                (parent.getChildAt(0) as TextView).textSize = 20f
                (parent.getChildAt(0) as TextView).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
                //---------------------------------------------------------------
            } else {
                Log.d("adapter child", "adapter child is null")
            }
        }
        if (binding.txtPriceValue.text !== "") btnOnClkCalculate(null)
    }

    override fun onNothingSelected(adapterView: AdapterView<*>?) {}

    fun onClickGoBack(view: View){
        finish()
    }

}