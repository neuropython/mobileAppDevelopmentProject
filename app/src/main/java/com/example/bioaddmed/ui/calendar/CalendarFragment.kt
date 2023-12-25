package com.example.bioaddmed.ui.calendar

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bioaddmed.databinding.FragmentCalendarBinding
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val calendarViewModel =
            ViewModelProvider(this).get(CalendarViewModel::class.java)

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Get references to views
        val calendarView: CalendarView = binding.calendarView
        val selectedDateTextView: TextView = binding.selectedDateTextView

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
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate)

                // Show the formatted date in the TextView
                selectedDateTextView.text = "Selected Date: $formattedDate"


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
