package com.example.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.models.Article

class NewsAdapter(private val onItemClickListener: (Article) -> Unit) :
    RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Article>() {

        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
            oldItem.url == newItem.url


        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
            oldItem == newItem

    }
    val diff = AsyncListDiffer(this, diffCallback)

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.articleImage)
        val title: TextView = itemView.findViewById(R.id.articleTitle)
        val description: TextView = itemView.findViewById(R.id.articleDescription)
        val source: TextView = itemView.findViewById(R.id.articleSource)
        val date: TextView = itemView.findViewById(R.id.articleDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return ArticleViewHolder(view)
    }

    override fun getItemCount(): Int = diff.currentList.size

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = diff.currentList[position]

        with(holder) {
            Glide.with(this.itemView).load(article.urlToImage).into(image)
            title.text = article.title
            description.text = article.description
            source.text = article.source.name
            date.text = article.publishedAt

            this.itemView.setOnClickListener {
                onItemClickListener(article)
            }
        }
    }
}