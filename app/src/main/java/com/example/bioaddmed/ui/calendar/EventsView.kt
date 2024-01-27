package com.example.bioaddmed.ui.calendar

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bioaddmed.R
import com.google.firebase.database.FirebaseDatabase


class EventsView : AppCompatActivity() {

    private var button4: Button? = null
    private var intent: Intent? = null
    private val databaseReference = FirebaseDatabase.getInstance().getReference("Calendar")

    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events_view)
        val receivedIntent = getIntent()
        val receivedDate = receivedIntent?.getStringExtra("date")
        Log.d("YourTag", "Received Date in EventsView: $receivedDate")
        val eventList = mutableListOf<EventsWithDesc>()


        val colorFrom = Color.RED
        val colorTo = Color.BLUE


        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.addUpdateListener { animator ->
            button4?.backgroundTintList = ColorStateList.valueOf(animator.animatedValue as Int)
        }
        colorAnimation.duration = 2000
        colorAnimation.repeatCount = ValueAnimator.INFINITE
        colorAnimation.repeatMode = ValueAnimator.REVERSE
        colorAnimation.start()

        databaseReference.child(receivedDate.toString()).get().addOnSuccessListener { dataSnapshot ->
            val event = dataSnapshot.value
            if (event == null) {
                val eventTextView = findViewById<TextView>(R.id.textView2)
                eventTextView.text = "No events"
            } else {
                Log.d("YourTag", "Event: ${event::class.java}")
                Log.d("YourTag", "Event: $event")

                if (event is Map<*, *>) {
                    for (innerValue in event) {
                        try {
                            val nodeClass = Class.forName("java.util.HashMap\$Node")
                            val fields = nodeClass.declaredFields
                            for (field in fields) {
                                field.isAccessible = true
                                val fieldName = field.name
                                val fieldValue = field.get(innerValue)
                                when (fieldName) {
                                    "value" -> {
                                        val eventMap = fieldValue as? Map<*, *>
                                        val eventName = eventMap?.get("eventName") as? String
                                        val eventTime = eventMap?.get("eventTime") as? String
                                        val eventDate = receivedDate
                                        val eventDescription = eventMap?.get("eventDescription") as? String
                                        val eventAuthor = eventMap?.get("eventAuthor") as? String
                                        Log.d("Second", "EventName: $eventName EventTime: $eventTime EventDate: $eventDate EventDescription: $eventDescription EventAuthor: $eventAuthor")
                                        addToRecyclerViewWithDescryption(
                                            eventName,
                                            eventTime,
                                            eventDate,
                                            eventDescription,
                                            eventAuthor
                                        )?.let {
                                            eventList.add(
                                                it
                                            )
                                        }
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("Error", "Error accessing HashMap\$Node properties: ${e.message}")
                        }
                    }
                }

                Log.d("Third", "EventList: $eventList")

                    val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewSecond)
                    recyclerView.layoutManager = LinearLayoutManager(this)
                    recyclerView.adapter = EventsActivityAdapter(eventList)


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
private fun addToRecyclerViewWithDescryption(
    eventName: String?,
    eventTime: String?,
    eventDate: String?,
    eventDescription: String?,
    eventAuthor: String?
): EventsWithDesc {
    return EventsWithDesc(eventName, eventTime, eventDate, eventDescription, eventAuthor)
}
