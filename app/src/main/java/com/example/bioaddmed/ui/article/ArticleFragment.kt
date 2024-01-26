package com.example.bioaddmed.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bioaddmed.databinding.FragmentArticleBinding
import com.example.bioaddmed.ui.aditional.CiteHttpRequest

class ArticleFragment : Fragment() {

    private var _binding: FragmentArticleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(ArticleViewModel::class.java)

        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        val httpRequest = CiteHttpRequest(CiteHttpRequest.OnTaskCompleted {
            fun onTaskCompleted(quote: String) {
                // Here you can use the quote
                textView.text = quote
            }
        })
        httpRequest.execute("https://api.kanye.rest/")
        return root
    }
}