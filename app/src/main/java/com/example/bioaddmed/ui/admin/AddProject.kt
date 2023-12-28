package com.example.bioaddmed.ui.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bioaddmed.MainActivity
import com.example.bioaddmed.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddProject : AppCompatActivity(), UserAdapter.UserAdapterListener {

    private val selectedNames: MutableList<String> = mutableListOf()
    private lateinit var selectedNamesTextView: TextView
    private lateinit var button: Button
    private lateinit var projectName: TextView
    private lateinit var databaseAddProject: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_project)

        // Assuming you have a TextView in your layout with id selectedNamesTextView
        selectedNamesTextView = findViewById(R.id.selectedUsers)
        button = findViewById(R.id.createProject)
        projectName = findViewById(R.id.projectName)
        databaseAddProject = FirebaseDatabase.getInstance().getReference("Project")

        readDataBase { usersList ->
            val recyclerView = findViewById<RecyclerView>(R.id.userRecycleView)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = UserAdapter(usersList, this)

            button.setOnClickListener() {
                val projectNameText = projectName.text.toString()
                databaseAddProject.child(projectNameText).setValue(selectedNames)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onChipPressed(user: UserData) {
        if (selectedNames.contains(user.name)) {
            selectedNames.remove(user.name)
        } else {
            selectedNames.add(user.name)
        }
        selectedNamesTextView.text = selectedNames.joinToString(", ")
    }

    fun readDataBase(callback: (List<UserData>) -> Unit) {
        val databaseReferenceUsers = FirebaseDatabase.getInstance().getReference("user")
        val allPeople = mutableListOf<UserData>()

        // Add a ValueEventListener to retrieve data
        databaseReferenceUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Handle the data here
                val data = dataSnapshot.value
                if (data is Map<*, *>) {
                    for (innerValue in data) {
                        try {
                            val user = UserData(innerValue.key.toString())
                            allPeople.add(user)
                        } catch (e: Exception) {
                            Log.e("Error", "Error accessing HashMap\$Node properties: ${e.message}")
                        }
                    }
                }
                callback(allPeople)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
                Log.e("Error", "Error getting data", databaseError.toException())
            }
        })
    }
}



