package com.example.bioaddmed.ui.calendar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.bioaddmed.R

class EventsView : AppCompatActivity() {

    private var button4: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events_view)

        button4 = findViewById(R.id.button4)

        button4?.setOnClickListener {
            val intent = Intent(this, SelectedDayActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        button4 = null
    }
}
