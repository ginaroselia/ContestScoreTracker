package com.example.thecontest

import android.content.res.Configuration
import android.graphics.Color
import android.media.MediaPlayer
import android.media.AudioAttributes
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    var operator = "operate"
    var count =0
    private lateinit var result:TextView

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE)
        {}
        else if (newConfig.orientation==Configuration.ORIENTATION_PORTRAIT)
        {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val score = findViewById<Button>(R.id.btnScore)
        val steal = findViewById<Button>(R.id.btnSteal)
        val reset = findViewById<Button>(R.id.btnReset)
        val result=findViewById<TextView>(R.id.tvResult)

        score.setOnClickListener {
            val addition = when (operator) {
               "operate" -> add(1, count)
                else -> add(1, count)
            }
            count=addition
            result.text = ""+count
            val colourNumber=(result.text?.toString()?.toInt()?:0)
            resultColorChange(colourNumber,result)
            Log.i("CYCLE", "Adding one score.")

            if (count<15){
                val mp1=MediaPlayer.create(this, R.raw.scoresound)
                mp1.start()
                mp1.setOnPreparedListener{
                    Log.i("MUSIC", "Scoring sound executed")
                }
            }
            if(count==15) {
                score.isEnabled = false
                steal.isEnabled = false
                Toast.makeText(this, resources.getString(R.string.congrats), Toast.LENGTH_SHORT).show()
                Log.i("CYCLE", "Stopping score and steal buttons.")
                val mp4 = MediaPlayer.create(this, R.raw.winningsound)
                mp4.start()
                mp4.setOnPreparedListener {
                    Log.i("MUSIC", "Winning sound executed")
                }
            }
        }

        reset.setOnClickListener {
            val mp2 =MediaPlayer.create(this, R.raw.resetsound)
            mp2.start()
            mp2.setOnPreparedListener{
                Log.i("MUSIC", "Resetting sound executed")
            }
            count = 0
            result.text=""+count
            val colourNumber=(result.text?.toString()?.toInt()?:0)
            resultColorChange(colourNumber,result)
            Log.i("CYCLE", "Restarting scores.")
            score.isEnabled = true
            steal.isEnabled = true
        }

        steal.setOnClickListener {
            if (count<=0){
                result.text="0"
                Toast.makeText(this, resources.getString(R.string.warning), Toast.LENGTH_LONG).show()
                Log.i("CYCLE", "Prohibiting negative values.")
            }
            else {
            val subtraction = when (operator) {
                "operate" -> minus(1, count)
                else -> minus(1, count)
            }
            val mp3=MediaPlayer.create(this, R.raw.stealsound)
            mp3.start()
            mp3.setOnPreparedListener{
                Log.i("MUSIC", "Stealing sound executed")
            }
            count=subtraction
            result.text = ""+count
            val colourNumber=(result.text?.toString()?.toInt()?:0)
            resultColorChange(colourNumber,result)}
            Log.i("CYCLE", "Stealing scores.")
        }
        savedInstanceState?.let {
            count=savedInstanceState.getInt("KEYCOUNT")
        }
    }
    override fun onSaveInstanceState(outState: Bundle){
        super.onSaveInstanceState(outState)
        outState.putInt("KEYCOUNT", count)
        Log.i("SAVELOG", "onSaveInstanceState $count")
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        count = savedInstanceState.getInt("KEYCOUNT")
        result.text = count.toString()
        Log.i("SAVELOG", "restoreInstanceState $count")
    }

    private fun add(number: Int, result: Int) = result + number
    private fun minus(number: Int, result: Int) = result-number
    fun resultColorChange(colorRange: Int, result:TextView){
        when (colorRange) {
            in 5..9 -> {result.setTextColor(Color.BLUE)}
            in 10..15 -> {result.setTextColor(Color.GREEN)}
            else -> {result.setTextColor(Color.BLACK)}
        }
    }
}



