package com.example.bioaddmed.ui

import android.annotation.SuppressLint
import android.app.UiModeManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.bioaddmed.MainActivity
import com.example.bioaddmed.R
import com.example.bioaddmed.ui.user.LogInActivity
import com.example.bioaddmed.ui.user.RegisterActivity

class StartingActivity : AppCompatActivity() {


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting)


        val registerButton: Button = findViewById(R.id.Register)
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish();
        }

        val loginButton: Button = findViewById(R.id.Login)
        loginButton.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
            finish();
        }
        val skipButton: Button = findViewById(R.id.Skip)
        skipButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish();
        }
        val uiModeManager = getSystemService(UiModeManager::class.java)
        uiModeManager?.nightMode = UiModeManager.MODE_NIGHT_YES
    }
}
