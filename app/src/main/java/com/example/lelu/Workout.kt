package com.example.lelu

import android.Manifest.permission.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.lelu.WorkoutHandler.Dip
import com.example.lelu.WorkoutHandler.PullUp
import com.example.lelu.WorkoutHandler.PushUp
import com.example.lelu.WorkoutHandler.Training
import com.example.lelu.databinding.ActivityWorkoutBinding


//toast: Toast.makeText(applicationContext,"message", Toast.LENGTH_LONG).show()

class Workout : AppCompatActivity() {

    private lateinit var training: Training
    private lateinit var pullUp: PullUp
    private lateinit var pushUp: PushUp
    private lateinit var dip: Dip

    private lateinit var timer: TimeHandler

    private val jsonHandler: JSONHandler = JSONHandler()
    private val fileHandler: FileHandler = FileHandler("appState.json")

    private lateinit var binding: ActivityWorkoutBinding

    private var saved = false
    private var id = -1 //id of workout, -1 if not saved, other number if saved

    //checking for each layer if expanded
    private val expanded = mutableMapOf<String,Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }

        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE), 1)
        }

        if (ContextCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(CAMERA), 1)
        }

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
        timer = TimeHandler(this, binding)

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
                    fileHandler.WriteToFile(jsonHandler.getJSON(training))
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
        Toast.makeText(applicationContext,fileHandler.ReadFromFile(), Toast.LENGTH_LONG).show()
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

    fun onClickChronometer(view: View) {

        timer.onClickChronometer(view)
    }

}

