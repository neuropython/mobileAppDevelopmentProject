package com.example.bioaddmed.ui.article

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.bioaddmed.R

class ArticleAdapter (private val articleList : List<ArticleData>)
    : RecyclerView.Adapter<ArticleAdapter.MyViewHolder>(){

    interface ArticleAdapterListener {
        fun onButtonPressed(article: ArticleData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =  LayoutInflater.from(parent.context)
            .inflate(R.layout.article_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentArticle = articleList[position]
        holder.title!!.text = currentArticle.title
        holder.title.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(currentArticle.link))
            it.context.startActivity(intent)
        }
    }
    class MyViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val title: android.widget.Button? = itemView.findViewById(R.id.articleButton)
    }
    fun getRandomColor(): Int {
        val rnd = java.util.Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }
    private fun animateButtonBackground(button: Button) {
        val colorFrom = getRandomColor()
        val colorTo = getRandomColor()

        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.addUpdateListener { animator ->
            button.backgroundTintList = ColorStateList.valueOf(animator.animatedValue as Int)
        }
        colorAnimation.duration = 2000
        colorAnimation.repeatCount = ValueAnimator.INFINITE
        colorAnimation.repeatMode = ValueAnimator.REVERSE
        colorAnimation.start()
    }
}