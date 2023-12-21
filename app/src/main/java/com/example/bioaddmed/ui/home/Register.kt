package com.example.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

//import intent
import android.content.Intent
//import button
import android.widget.Button
//import switch
import android.widget.Switch
//import firebase
import com.google.firebase.database.DatabaseReference
//import child
import com.google.firebase.database.ktx.database
//import ktx
import com.google.firebase.ktx.Firebase
//import edit text
import android.widget.EditText
import com.example.bioaddmed.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Pattern



class RegisterActivity : AppCompatActivity() {

    @SuppressLint("UseSwitchCompatOrMaterialCode", "RestrictedApi", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val register = findViewById<Button>(R.id.button2)
        val switch = findViewById<Switch>(R.id.switch1)

//        write a function that enables a button if the switch is checked
        switch.setOnClickListener {
            if (switch.isChecked) {
                register.isEnabled = true
            } else {
                register.isEnabled = false
            }
        }

//

        register.isEnabled = switch.isChecked


        val email = findViewById<EditText>(R.id.editTextTextEmailAddress2)
        val name = findViewById<EditText>(R.id.editTextText)
        val phone = findViewById<EditText>(R.id.editTextPhone)
        val password = findViewById<EditText>(R.id.editTextTextPassword2)

        val DatabaseReference: DatabaseReference = Firebase.database.reference
        val userId = DatabaseReference.push().key.toString()
//        create click listener for register button
        val databaseReference = FirebaseDatabase.getInstance().reference


        register.setOnClickListener {
            val name_text = name.text.toString()
            val email_text = email.text.toString()
            val phone_text = phone.text.toString()
            val password_text = password.text.toString()
            //write function that checks if email is valid

            fun isEmailValid(email: String): Boolean {
                val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
                val pattern = Pattern.compile(emailRegex)
                val matcher = pattern.matcher(email)
                return matcher.matches()
            }

            fun containsSpecialCharacter(password: String): Boolean {
                val specialCharacters = setOf('!', '@', '%', '^', '&', '*', '(')
                return password.any { it in specialCharacters }
            }

            when {
                !isEmailValid(email_text) -> {
                    email.error = "Please enter a valid email"
                    email.requestFocus()
                }

                name_text.isEmpty() -> {
                    name.error = "Please enter your name"
                    name.requestFocus()
                }

                phone_text.isEmpty() || phone_text.length != 9 -> {
                    phone.error = "Please enter a valid phone number"
                    phone.requestFocus()
                }

                password_text.isEmpty() -> {
                    password.error = "Please enter a valid password"
                    phone.requestFocus()
                }

                password_text.length !in 6..12 -> {
                    password.error = "Password must be between 6 and 12 characters"
                    phone.requestFocus()
                }

                !password_text.contains(Regex("[0-9]")) -> {
                    password.error = "Password must contain at least one number"
                    phone.requestFocus()
                }

                !password_text.contains(Regex("[A-Z]")) -> {
                    password.error = "Password must contain at least one capital letter"
                    phone.requestFocus()
                }

                !password_text.contains(Regex("[a-z]")) -> {
                    password.error = "Password must contain at least one lowercase letter"
                    phone.requestFocus()
                }

                !containsSpecialCharacter(password_text) -> {
                    password.error = "Password must contain at least one special character"
                    phone.requestFocus()
                }

                else -> {
                    val email_to_db = email_text.replace(".", ",")
                    val user = User(name_text, email_text, phone_text, password_text)
                    databaseReference.child("user").child(email_to_db).setValue(user)
                    val intent = Intent(this, LogInActivity::class.java)
                    startActivity(intent)

                }
            }
        }
    }

    class User(
        val name: String?,
        val email: String?,
        val phone: String?,
        val password: String?
    ) {
        // Default constructor required for Firebase
        constructor() : this("", "", "", "")
    }

}

