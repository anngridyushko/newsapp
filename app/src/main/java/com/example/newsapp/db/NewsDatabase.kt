package com.example.newsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp.models.Article
import com.example.newsapp.util.Constants

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(SourceTypeConverter::class)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun getArticleDAO(): ArticleDAO

    companion object {

        @Volatile
        private var instance: NewsDatabase? = null

        private const val DB_NAME = Constants.NEWS_DB_NAME
        private val Lock = Any()

        private fun createDB(context: Context): NewsDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                NewsDatabase::class.java,
                DB_NAME,
            ).build()
        }

        fun getInstance(context: Context): NewsDatabase {
            return instance ?: synchronized(Lock) {
                instance ?: createDB(context).also {
                    instance = it
                }
            }
        }
    }
}