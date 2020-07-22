package com.example.mvvmmypos.model

import androidx.room.*

@Dao
interface PromotionDao {
    @Query("SELECT * FROM promotion")
    fun getAll(): List<Promotion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(price: Promotion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(promotion: List<Promotion>)

    @Query("DELETE FROM promotion")
    fun deleteAll()
}