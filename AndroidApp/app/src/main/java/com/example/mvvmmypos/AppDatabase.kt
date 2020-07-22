package com.example.mvvmmypos

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import com.example.mvvmmypos.model.*

@Database(entities = [Product::class, Price::class, Promotion::class,Sale::class, SaleItem::class, SaleServerMap::class, Sync::class], version = 8)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun priceDao(): PriceDao
    abstract fun promotionDao(): PromotionDao
    abstract fun saleDao(): SaleDao
    abstract fun saleItemDao(): SaleItemDao
    abstract fun saleServerMapDao(): SaleServerMapDao
    abstract fun syncDao(): SyncDao

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "retail.db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}