package com.example.bioaddmed.ui.calendar

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.bioaddmed.databinding.FragmentCalendarBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private lateinit var intent: Intent

    private var clickCounter = 0
    private lateinit var selectedDate: Date
    private val handler = Handler()
    private lateinit var RecyclerView : RecyclerView
    private lateinit var newArrayLst : ArrayList<EventsWithoutDesc>
    private lateinit var date : MutableList<String>
    private lateinit var name : MutableList<String>
    private lateinit var time : MutableList<String>



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val calendarViewModel =
            ViewModelProvider(this).get(CalendarViewModel::class.java)
        getFromFirebase()

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Get references to views
        val calendarView: CalendarView = binding.calendarView
        val recyclerView: RecyclerView = binding.recyclerView

        // Set up the OnDateChangeListener
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            clickCounter++

            // If it's the first click, post a delayed action to reset the counter
            if (clickCounter == 1) {
                handler.postDelayed({
                    clickCounter = 0
                }, DOUBLE_CLICK_INTERVAL.toLong())
            }

            // If it's the second click, handle the selected date
            if (clickCounter == 2) {
                selectedDate = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }.time

                // Format the selected date (you can change the format as needed)
                val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate).toString()
                Log.d("YourTag", "Formatted Date in CalendarFragment: $formattedDate")


                // Show the formatted date in the TextView
                intent = Intent(activity, EventsView::class.java)
                intent.putExtra("date", formattedDate)
                startActivity(intent)


                // Reset the counter
                clickCounter = 0
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val DOUBLE_CLICK_INTERVAL = 500 // Time in milliseconds
    }
}
fun getFromFirebase() {
    val databaseReference = FirebaseDatabase.getInstance().getReference("Calendar")


    val currentDate = getCurrentDate()
    val currentYear = SimpleDateFormat("yyyy", Locale.US).format(currentDate)
    val currentMonth = SimpleDateFormat("MM", Locale.US).format(currentDate)

    databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
        @SuppressLint("SoonBlockedPrivateApi")
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val yearData = dataSnapshot.child(currentYear).value as? Map<String, Any>

            if (yearData == null || yearData.isEmpty()) {
                Log.d("Firebase1", "No events for the current year")
            } else {
                val monthData = yearData[currentMonth] as? Map<String, Any>
                if (monthData == null || monthData.isEmpty()) {
                    Log.d("Firebase2", "No events for the current month")
                } else {
                    val calendar = Calendar.getInstance()
                    calendar.time = currentDate
                    val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                    val startDayOfWeek = calendar.firstDayOfWeek

                    // Calculate the difference between the current day and the start of the week
                    val daysUntilStartOfWeek = (currentDayOfWeek - startDayOfWeek + 7) % 7

                    // Calculate the start date of the week
                    calendar.add(Calendar.DAY_OF_YEAR, -daysUntilStartOfWeek)
                    val startOfWeek = calendar.time

                    // Calculate the end date of the week (7 days from the start)
                    calendar.add(Calendar.DAY_OF_YEAR, 6)
                    val endOfWeek = calendar.time
                    var dayKeyInt = SimpleDateFormat("dd", Locale.US).format(startOfWeek).toInt()
                    val enddayKeyInt = SimpleDateFormat("dd", Locale.US).format(endOfWeek).toInt()
                    var dayKey = SimpleDateFormat("dd", Locale.US).format(startOfWeek).toInt()
                    // Loop through the days of the week and log events
                    while (dayKeyInt <= enddayKeyInt) {
                        var dupa = dayKeyInt.toString()
                        if (dupa.length == 1) {
                            dupa = "0$dayKeyInt"
                        }
                        Log.d("Firebase3", "Events for $dupa:")
                        for ((key, value) in monthData) {
                            if (key.trim().equals(dupa, ignoreCase = true)) {
                                Log.d("Firebase3", "${value::class.java}")

                                if (value is ArrayList<*>) {
                                    for (innerValue in value) {
                                        if (innerValue is Map<*, *>) {
                                            val eventName = innerValue["eventName"] as? String
                                            val eventTime = innerValue["eventTime"] as? String
                                            Log.d("Damian", "Event Name: $eventName, Event Time: $eventTime")
                                        } }
                                }
                                if (value is Map<*, *>) {
                                    for (innerValue in value) {
                                        for (innerValue in value) {
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

                                                            Log.d("Damian", "Event Name: $eventName, Event Time: $eventTime")
                                                        }
                                                        // Add more cases as needed for other fields
                                                    }
                                                }
                                            } catch (e: Exception) {
                                                Log.e("Damian", "Error accessing HashMap\$Node properties: ${e.message}")
                                            }
                                        }
                                    }

                                }
                            }
                        }
                        dayKeyInt += 1
                        calendar.add(Calendar.DAY_OF_YEAR, 1)
                    }
                }
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.e("Firebase", "Error getting data", databaseError.toException())
        }
    })
}


private fun getCurrentDate(): Date {
    return Calendar.getInstance().time
}
