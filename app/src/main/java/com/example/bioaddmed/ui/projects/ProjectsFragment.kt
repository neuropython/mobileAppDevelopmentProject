package com.example.bioaddmed.ui.projects

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bioaddmed.databinding.FragmentProjectsBinding
import com.google.firebase.auth.FirebaseAuth


class ProjectsFragment : Fragment() {

    private var _binding: FragmentProjectsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var user = FirebaseAuth.getInstance().currentUser
    val databaseReferance = com.google.firebase.database.FirebaseDatabase.getInstance().getReference("Project")



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(ProjectsViewModel::class.java)

        _binding = FragmentProjectsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        val recyclerView = binding.projectRecyclerView

        databaseReferance.get().addOnSuccessListener {
            val list = ArrayList<ProjectData>()
            val value = it.value
            for (i in value as HashMap<String, Any>) {
                val name = i.key
                val peopleList = i.value as ArrayList<String>
                list.add(ProjectData(name, peopleList))
                recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
                recyclerView.setHasFixedSize(true)
                recyclerView.adapter = ProjectAdapter(list)
            }

        } .addOnFailureListener {
            Log.d("TAG", "onCreateView: " + it.message)
        }
        Log.d("TAG", "onCreateView: " + (user!!.displayName))

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}