package com.example.bioaddmed.ui.user

//import intent
//import button
//import switch
//import firebase
//import child
//import ktx
//import edit text
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bioaddmed.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern


class RegisterActivity : AppCompatActivity() {

    @SuppressLint("UseSwitchCompatOrMaterialCode", "RestrictedApi", "MissingInflatedId")
    private lateinit var auth: FirebaseAuth;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val register = findViewById<Button>(R.id.button2)
        val switch = findViewById<Switch>(R.id.switch1)

//        write a function that enables a button if the switch is checked
        switch.setOnClickListener {
            register.isEnabled = switch.isChecked
        }

//

        register.isEnabled = switch.isChecked


        val email = findViewById<EditText>(R.id.editTextTextEmailAddress2)
        val name = findViewById<EditText>(R.id.editTextText)
        val phone = findViewById<EditText>(R.id.editTextPhone)
        val password = findViewById<EditText>(R.id.editTextTextPassword2)
        auth = Firebase.auth

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
                    auth.createUserWithEmailAndPassword(email_text, password_text)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(name_text)
                                    .build()
                                user?.updateProfile(profileUpdates)
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(
                                    baseContext,
                                    "Authentication Successful.",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(
                                    baseContext,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }
                    val user = User(name_text, email_text, phone_text)
                    databaseReference.child("user").child(name_text).setValue(user)
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
    ) {
        constructor() : this("", "", "")

    }    }
        // Default constructor required for Firebase



