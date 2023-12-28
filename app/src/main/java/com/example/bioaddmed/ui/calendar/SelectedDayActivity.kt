package com.example.bioaddmed.ui.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bioaddmed.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SelectedDayActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_day)
        val receivedIntent = getIntent()
        val receivedDate = receivedIntent?.getStringExtra("date")

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val eventName = findViewById<EditText>(R.id.eventName)
        val eventDescription = findViewById<EditText>(R.id.eventDescryption)
        val eventTime = findViewById<EditText>(R.id.eventTime)
        val submitButton = findViewById<Button>(R.id.submit)
        val eventAuthor = findViewById<EditText>(R.id.userName)
        val DatabaseReference: DatabaseReference? = null

        submitButton.setOnClickListener() {
            val eventNameText = eventName.text.toString()
            val eventDescriptionText = eventDescription.text.toString()
            val eventTimeText = eventTime.text.toString()
            val eventAuthor = eventAuthor.text.toString()
            val event = Event(eventDescriptionText, eventTimeText,eventNameText, eventAuthor)
            if (eventNameText.isEmpty() || eventDescriptionText.isEmpty()
                || eventTimeText.isEmpty() || eventAuthor.isEmpty()) {
                var toast = Toast.makeText(this, "Please fill all fields",
                    Toast.LENGTH_SHORT)
                toast?.show()
            } else {val DatabaseReference =
                FirebaseDatabase.getInstance().getReference("Calendar")
                DatabaseReference.child(receivedDate.toString()).child(eventNameText).
                setValue(event)

                var toast = Toast
                    .makeText(this, "Event added", Toast.LENGTH_SHORT)
                toast?.show()
            }
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
