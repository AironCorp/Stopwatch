package ru.aironcorp.stopwatch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class MainActivity : AppCompatActivity() {

    var handler: Handler? = null

    var seconds = 0
    var minutes = 0
    var hours = 0
    var millisecond = 0

    var milliseconds : Long = 0
    var millisecondsSave : Long = 0
    var startTime: Long = 0

    var running = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonStart?.onClick {
            if (running) {
                startTime = SystemClock.uptimeMillis()
                buttonStart.setText(R.string.button_title_stop)
                handler?.postDelayed(runnable, 0)
                running = false
                buttonReset.isClickable = false
                buttonReset.setTextColor(resources.getColor(R.color.colorGrey))
                separator.visibility = View.VISIBLE
            }
            else {
                millisecondsSave = milliseconds
                handler?.removeCallbacks(runnable)
                buttonStart.setText(R.string.button_title_start)
                running = true
                buttonReset.isClickable = true
                buttonReset.setTextColor(resources.getColor(R.color.colorBlack))
                separator.visibility = View.VISIBLE
            }
        }

        buttonReset?.onClick {
            milliseconds = 0
            millisecondsSave = 0
            startTime = 0
            running = true
            textHours.text = resources.getText(R.string.default_time)
            textMinutes.text = resources.getText(R.string.default_time)
            textSeconds.text = resources.getText(R.string.default_time)
            textMilliseconds.text = resources.getText(R.string.default_time)
            buttonStart.setText(R.string.button_title_start)
            separator.visibility = View.VISIBLE
        }

        handler = Handler()
    }

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            if (millisecondsSave == 0.toLong())
                milliseconds = SystemClock.uptimeMillis() - startTime
            else milliseconds = (SystemClock.uptimeMillis() - startTime)+millisecondsSave

            millisecond = (milliseconds % 100).toInt()
            seconds = (milliseconds/1000).toInt()%60
            minutes = (milliseconds/60000).toInt()
            hours = minutes/60

            if(seconds%2 == 0)
                separator.visibility = View.INVISIBLE
            else
                separator.visibility = View.VISIBLE

            var secondsText = "$seconds"
            var minutesText = "$minutes"
            var hoursText = "$hours"
            var millisecondText = "$millisecond"

            if (millisecondText.length < 2) {
                millisecondText = "0$millisecondText"
            }
            if (secondsText.length < 2) {
                secondsText = "0$secondsText"
            }
            if (minutesText.length < 2) {
                minutesText = "0$minutesText"
            }
            if (hoursText.length < 2) {
                hoursText = "0$hoursText"
            }

            textMilliseconds.text = millisecondText
            textSeconds.text = secondsText
            textMinutes.text = minutesText
            textHours.text = hoursText

            handler?.postDelayed(this, 0)
        }
    }
}