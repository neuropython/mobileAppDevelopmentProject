package com.example.bioaddmed.ui.admin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.bioaddmed.MainActivity
import com.example.bioaddmed.R

class AdminActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_activity)
        val adminButton = findViewById<Button>(R.id.adminButton)
        adminButton.setOnClickListener() {
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
        }
        val calendarButton = findViewById<Button>(R.id.calendarButton)
        calendarButton.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}