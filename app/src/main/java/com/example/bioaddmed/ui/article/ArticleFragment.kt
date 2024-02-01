package com.example.bioaddmed.ui.article

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bioaddmed.databinding.FragmentArticleBinding
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

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

        val articleList = ArrayList<ArticleData>()

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = "Articles"
        }
        Log.d("TAG", "onCreateView: ")
        val URL = "https://bioaddmed-baza-wiedzy.onrender.com/api/articles"
        if (URL.isNotEmpty()) {
            Log.d("TAG", "onCreateView: " + URL)
            val articleDataFetch = OkHttpClient()
            val request = Request.Builder()
                .url(URL)
                .addHeader("Auth-Key", "B10@dDM3d1229")
                .build()
            articleDataFetch.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                    textView.text = "Failed to fetch data!"
                    Log.d("error", "onResponse: ")
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {

                    response.use {
                        if (!response.isSuccessful) throw java.io.IOException("Unexpected code $response")
                        else {
                            val body = response.body?.string()
                            val jsonObject = JSONObject(body)
                            val articles = jsonObject.getJSONArray("articles")
                            for (i in 0 until articles.length()) {
                                val article = articles.getJSONObject(i)
                                val title = article.getString("title")
                                val link = article.getString("link")
                                articleList.add(ArticleData(title, link))
                                Log.d("TAG", "onResponse: " + articleList)

                            }
                        }
                    }
                    activity?.runOnUiThread {
                    val recyclerView = binding.recyclerView2
                    recyclerView.layoutManager = LinearLayoutManager(activity)
                    recyclerView.setHasFixedSize(true)
                    recyclerView.adapter = ArticleAdapter(articleList)
                    }
                }
            }
            )
        }

        else {
            textView.text = "No URL"
        }



        return root
    }
}