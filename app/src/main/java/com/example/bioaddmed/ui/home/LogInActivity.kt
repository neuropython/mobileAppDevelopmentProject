package com.example.bioaddmed.ui.home

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.util.Log
import com.example.bioaddmed.R
import com.example.bioaddmed.ui.dashboard.DashboardFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class LogInActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<android.widget.EditText>(R.id.editTextTextEmailAddress)
        val password = findViewById<android.widget.EditText>(R.id.editTextTextPassword)
        val login = findViewById<Button>(R.id.button)
        val intent = Intent(this, DashboardFragment::class.java)

        login.setOnClickListener {
            val emailText = email.text.toString()
            val passwordText = password.text.toString()
            val DatabaseReference: DatabaseReference = Firebase.database.reference.child("user").child(emailText)

            DatabaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val passwordDb = dataSnapshot.child("password").getValue(String::class.java)
                    val passwordDbText = passwordDb.toString()
                    Log.d(TAG, "Value1 is: $passwordDbText")
                    Log.d(TAG, "Value2 is: $passwordText")
                    if (passwordDb == null ) {
                        email.error = "Email is not recognized" }
                    else {
                        if (passwordText == passwordDbText) {
                            intent.putExtra("eMail", emailText)
                            startActivity(intent)
                        }
                        else {
                            password.error = "Password is incorrect"
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })


        }
    }
}