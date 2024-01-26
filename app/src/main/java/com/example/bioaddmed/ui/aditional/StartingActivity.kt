package com.example.bioaddmed.ui.aditional

import android.annotation.SuppressLint
import android.app.UiModeManager
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.bioaddmed.R
import com.example.bioaddmed.ui.user.LogInActivity
import com.example.bioaddmed.ui.user.RegisterActivity

class StartingActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting)
        val constraintLayout = findViewById<ConstraintLayout>(R.id.mainLayout)
        val animationDrawable = constraintLayout.background as AnimationDrawable

        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(2000)
        animationDrawable.start()

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

        val textView = findViewById<android.widget.TextView>(R.id.Quote)
        val httpRequest = CiteHttpRequest(object : CiteHttpRequest.OnTaskCompleted {
            override fun onTaskCompleted(quote: String) {
                Log.d("DAMIAN!@#$", quote)
                textView.text = quote
            }
        })

        httpRequest.execute("https://api.kanye.rest/")
        val uiModeManager = getSystemService(UiModeManager::class.java)
        uiModeManager?.nightMode = UiModeManager.MODE_NIGHT_YES
    }
}

