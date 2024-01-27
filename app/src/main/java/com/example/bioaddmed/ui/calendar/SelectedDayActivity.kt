package com.example.bioaddmed.ui.calendar

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bioaddmed.R
import com.google.firebase.database.FirebaseDatabase

class SelectedDayActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_day)
        val receivedIntent = getIntent()
        val receivedDate = receivedIntent?.getStringExtra("date")

        val eventName = findViewById<EditText>(R.id.eventName)
        val eventDescription = findViewById<EditText>(R.id.eventDescryption)
        val eventTime = findViewById<EditText>(R.id.eventTime)
        val submitButton = findViewById<Button>(R.id.submit)
        val eventAuthor = findViewById<EditText>(R.id.userName)

        val colorFrom = Color.RED
        val colorTo = Color.BLUE

        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.addUpdateListener { animator ->
            submitButton.backgroundTintList = ColorStateList.valueOf(animator.animatedValue as Int)
        }
        colorAnimation.duration = 2000
        colorAnimation.repeatCount = ValueAnimator.INFINITE
        colorAnimation.repeatMode = ValueAnimator.REVERSE
        colorAnimation.start()

        submitButton.setOnClickListener() {
            val eventNameText = eventName.text.toString()
            val eventDescriptionText = eventDescription.text.toString()
            val eventTimeText = eventTime.text.toString()
            val eventAuthor = eventAuthor.text.toString()
            val event = Event(eventDescriptionText, eventTimeText, eventNameText, eventAuthor)
            if (eventNameText.isEmpty() || eventDescriptionText.isEmpty()
                || eventTimeText.isEmpty() || eventAuthor.isEmpty()
            ) {
                val toast = Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT)
                toast?.show()
            }

            if (!isTimeFormatValid(eventTimeText)) {
                Log.d("SelectedDayActivity", "Event added")
                val toast = Toast.makeText(this, "Invalid time format", Toast.LENGTH_SHORT)
                toast?.show()
            } else {
                val databaseReference = FirebaseDatabase.getInstance().getReference("Calendar")
                databaseReference.child(receivedDate.toString()).child(eventNameText)
                    .setValue(event)
                var toast = Toast.makeText(this, "Event added", Toast.LENGTH_SHORT)
                toast?.show()
            }
        }
    }

class Event(
    val eventDescription: String,
    val eventTime: String,
    val eventName: String,
    val eventAuthor: String,
) {
    constructor() : this("", "", "", "")
}

fun isTimeFormatValid(timeInput: String): Boolean {
    return true
}
}
