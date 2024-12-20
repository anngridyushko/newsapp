package com.example.newsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "article"
)
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val author: String? = "",       // can be null in some articles
    val content: String? = "",      // can be null in some articles
    val description: String? = "",  // can be null in some articles
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String? = "",          // can be null in some articles
    val urlToImage: String? = ""    // can be null in some articles
) : Serializable {

}