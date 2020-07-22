package com.example.mvvmmypos.model

import androidx.room.*

@Dao
interface PriceDao {
    @Query("SELECT * FROM price")
    fun getAll(): List<Price>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(price: Price)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(price: List<Price>)

    @Query("DELETE FROM price")
    fun deleteAll()
}