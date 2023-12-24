package com.example.bioaddmed.ui.calendar


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bioaddmed.R

class CalendarViewModel : ViewModel() {

    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String> = _selectedDate

    fun onDateSelected(year: Int, month: Int, dayOfMonth: Int) {
        val formattedDate = "$dayOfMonth/${month + 1}/$year"
        _selectedDate.value = formattedDate
    }
}

class MainActivity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var gestureDetector: GestureDetector

    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendarView = findViewById(R.id.calendarView)

        // Inicjalizacja GestureDetector
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                // Obsługa podwójnego kliknięcia
                handleDoubleTap(e)
                return super.onDoubleTap(e)
            }
        })

        // Ustawienie słuchacza dotyku dla CalendarView
        calendarView.setOnTouchListener { _, event ->
            // Przekazanie zdarzenia do GestureDetector
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    private fun handleDoubleTap(event: MotionEvent?) {
        // Tutaj obsługuje się podwójne kliknięcie

        // Pobranie daty z CalendarView
        val date = calendarView.date

        // Wyświetlenie wiadomości z wybraną datą
        val msg = "Double-clicked date is $date"

        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
    }
}