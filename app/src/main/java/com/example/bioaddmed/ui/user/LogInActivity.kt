package com.example.bioaddmed.ui.user

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bioaddmed.MainActivity
import com.example.bioaddmed.R
import com.example.bioaddmed.ui.admin.AdminActivity
import com.example.bioaddmed.ui.article.ArticleFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class LogInActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast", "MissingInflatedId")
    private lateinit var auth: FirebaseAuth
    val DatabaseReference: DatabaseReference = Firebase.database.reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<android.widget.EditText>(R.id.editTextTextEmailAddress)
        val password = findViewById<android.widget.EditText>(R.id.editTextTextPassword)
        val login = findViewById<Button>(R.id.button)
        var intent = Intent(this, ArticleFragment::class.java)
        auth = Firebase.auth

        login.setOnClickListener {
            val emailText = email.text.toString()
            val passwordText = password.text.toString()

            if (TextUtils.isEmpty(emailText)) {
                email.setError("Email is required")
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(passwordText)) {
                password.setError("Password is required")
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        check_if_admin(emailText)
                        intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        Toast.makeText(
                            baseContext,
                            "Authentication successful.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
            // Your code for handling non-empty email and password goes here
        }

        }

    private fun check_if_admin(emailText: String) {
        val email_to_db = emailText.replace(".", ",")
        val databaseReference = Firebase.database.reference
        val admin = databaseReference.child("Admin").child(email_to_db)
        admin.get().addOnSuccessListener {
            if (it.exists()) {
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
            }
        }

    }

}
