package com.example.bioaddmed.ui.calendar

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bioaddmed.R
import com.google.firebase.database.FirebaseDatabase


class EventsView : AppCompatActivity() {

    private var button4: Button? = null
    private var intent: Intent? = null
    private val DatabaseReference = FirebaseDatabase.getInstance().getReference("Calendar")



    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events_view)
        val receivedIntent = getIntent()
        val receivedDate = receivedIntent?.getStringExtra("date")
        Log.d("YourTag", "Received Date in EventsView: $receivedDate")

        DatabaseReference.child(receivedDate.toString()).get().addOnSuccessListener {
            val event = it.value?.toString()
            if (event == null || event.isEmpty()) {
                val eventTextView = findViewById<TextView>(R.id.textView2)
                eventTextView.text = "No events"
            } else {
                val eventTextView = findViewById<TextView>(R.id.textView2)
                eventTextView.text = event
            }
        }.addOnFailureListener {
            val eventTextView = findViewById<TextView>(R.id.textView2)
            eventTextView.text = "Error"
        }


        button4 = findViewById(R.id.button4)

        button4?.setOnClickListener {
            val intent = Intent(this, SelectedDayActivity::class.java)
            intent.putExtra("date", receivedDate)
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        button4 = null
    }
}
