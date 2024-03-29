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
import androidx.recyclerview.widget.LinearLayoutManager
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
    val newArrayList = mutableListOf<EventsWithoutDesc>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val calendarViewModel =
            ViewModelProvider(this).get(CalendarViewModel::class.java)

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = CalendarFragmentAdapter(newArrayList)

        getEventsFromFirebase { eventList ->
            newArrayList.clear()
            newArrayList.addAll(eventList)
            recyclerView.adapter?.notifyDataSetChanged()
        }

        val calendarView: CalendarView = binding.calendarView

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            clickCounter++

            if (clickCounter == 1) {
                handler.postDelayed({
                    clickCounter = 0
                }, DOUBLE_CLICK_INTERVAL.toLong())
            }

            if (clickCounter == 2) {
                selectedDate = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }.time

                val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate).toString()

                intent = Intent(activity, EventsView::class.java)
                intent.putExtra("date", formattedDate)
                startActivity(intent)

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
        private const val DOUBLE_CLICK_INTERVAL = 500
    }

    private fun getEventsFromFirebase(callback: (List<EventsWithoutDesc>) -> Unit) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Calendar")
        val eventList = mutableListOf<EventsWithoutDesc>()
        val currentDate = getCurrentDate()
        val currentYear = SimpleDateFormat("yyyy", Locale.US).format(currentDate)
        val currentMonth = SimpleDateFormat("MM", Locale.US).format(currentDate)

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SoonBlockedPrivateApi")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val yearData = dataSnapshot.child(currentYear).value as? Map<String, Any>

                if (yearData.isNullOrEmpty()) {
                    Log.d("Firebase1", "No events for the current year")
                } else {
                    val monthData = yearData[currentMonth] as? Map<String, Any>

                    if (monthData.isNullOrEmpty()) {
                        Log.d("Firebase2", "No events for the current month")
                    } else {
                        val calendar = Calendar.getInstance()
                        val endCalendar = Calendar.getInstance()
                        calendar.time = currentDate
                        val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                        val startDayOfWeek = calendar.firstDayOfWeek

                        val daysUntilStartOfWeek = (currentDayOfWeek - startDayOfWeek + 7) % 7
                        calendar.add(Calendar.DAY_OF_YEAR, -daysUntilStartOfWeek)
                        val startOfWeek = calendar
                        endCalendar.add(Calendar.DAY_OF_YEAR, 6)
                        val endOfWeek = endCalendar
                        Log.d("Start End", "StartOfWeek: ${startOfWeek.timeInMillis} EndOfWeek: ${endOfWeek.timeInMillis}")

                        while (startOfWeek.timeInMillis <= endOfWeek.timeInMillis) {
                            var dayToCheck = calendar.get(Calendar.DAY_OF_MONTH).toString()
                            if (dayToCheck.length == 1) {
                                dayToCheck = "0$dayToCheck"
                            }
                            for ((key, value) in monthData) {
                                if (key.trim().equals(dayToCheck, ignoreCase = true)) {
                                    if (value is ArrayList<*>) {
                                        for (innerValue in value) {
                                            if (innerValue is Map<*, *>) {
                                                val eventName = innerValue["eventName"] as? String
                                                val eventTime = innerValue["eventTime"] as? String
                                                val eventDate = dayToCheck.toInt()
                                                addToRecyclerView(eventName, eventTime, eventDate)?.let {
                                                    eventList.add(
                                                        it
                                                    )
                                                }

                                            } }
                                    }
                                    if (value is Map<*, *>) {
                                        for (innerValue in value) {
                                                try {
                                                    val eventDictionary: MutableMap<String, Map<String, String>> = mutableMapOf()
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
                                                                val eventDate = dayToCheck.toInt()

                                                                addToRecyclerView(eventName, eventTime, eventDate)?.let {
                                                                    eventList.add(
                                                                        it
                                                                    )
                                                                }

                                                            }
                                                            // Add more cases as needed for other fields
                                                        }
                                                    }
                                                } catch (e: Exception) {
                                                    Log.e("Error", "Error accessing HashMap\$Node properties: ${e.message}")
                                                }

                                        }

                                    }
                                }
                            }
                            calendar.add(Calendar.DAY_OF_YEAR, 1)
                        }
                    }
                }
                callback(eventList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Firebase", "Error getting data", databaseError.toException())
                callback(emptyList())
            }
        })
    }

    private fun handleEventMap(innerValue: Map<*, *>, dayKeyInt: Int, eventList: MutableList<EventsWithoutDesc>) {
        val eventName = innerValue["eventName"] as? String
        val eventTime = innerValue["eventTime"] as? String
        val eventDate = dayKeyInt
        Log.d("Firebase3", "EventName: $eventName EventTime: $eventTime EventDate: $eventDate")
        addToRecyclerView(eventName, eventTime, eventDate)?.let {
            eventList.add(it)
        }
    }

    private fun addToRecyclerView(eventName: String?, eventTime: String?, eventDate: Int): EventsWithoutDesc? {
        return eventTime?.let { EventsWithoutDesc(eventName, it, eventDate.toString()) }
    }

    private fun getCurrentDate(): Date {
        return Calendar.getInstance().time
    }
}